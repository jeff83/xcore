package com.cmbc.service;
import static ch.ralscha.extdirectspring.annotation.ExtDirectMethodType.STORE_READ;

import java.util.*;

import ch.ralscha.extdirectspring.filter.Filter;
import com.cmbc.entity.*;
import org.hibernate.jpa.criteria.predicate.BooleanExpressionPredicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import ch.ralscha.extdirectspring.annotation.ExtDirectMethod;
import ch.ralscha.extdirectspring.bean.ExtDirectStoreReadRequest;
import ch.ralscha.extdirectspring.bean.ExtDirectStoreResult;
import ch.ralscha.extdirectspring.filter.StringFilter;
import com.cmbc.security.JpaUserDetails;
import ch.rasc.edsutil.BaseCRUDService;
import ch.rasc.edsutil.QueryUtil;
import ch.rasc.edsutil.bean.ExtDirectStoreValidationResult;
import ch.rasc.edsutil.bean.ValidationError;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.SearchResults;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;

import javax.persistence.criteria.*;

@Service
@Lazy
@PreAuthorize("hasRole('ADMIN')")
public class DepartmentService extends BaseCRUDService<Department> {

    @Autowired
    private MessageSource messageSource;

    @Override
    @ExtDirectMethod(STORE_READ)
    @Transactional(readOnly = true)
    public ExtDirectStoreResult<Department> read(ExtDirectStoreReadRequest request) {
        request.getPage()

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Department> query  =  criteriaBuilder.createQuery(Department.class);
        Root<Department> root = query.from(Department.class);
        if (!request.getFilters().isEmpty()) {
            Iterator<Filter> filters = request.getFilters().iterator();
            List<Predicate> predicates = new ArrayList<>();
            while (filters.hasNext()){
                Filter filter = filters.next();
                Path<Object> path = root.get(filter.getField());
                if(filter instanceof StringFilter){
                    StringFilter stringFilter = (StringFilter) filter;
                    if(!StringUtils.isEmpty(stringFilter.getValue())){
                        Predicate predicate = criteriaBuilder.equal(path, stringFilter.getValue());
                        predicates.add(predicate);
                    }
                }
            }
            Predicate condition = criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
            query.where(condition);
            List<Department> aa = entityManager.createQuery(query).getResultList();
         }


        JPQLQuery query = new JPAQuery(entityManager).from(QUser.user);
        //查询条件生成，有两种方式，一种根据前台转值，自动循环添加到where条件中。

        /**
         * queryDSL适合手工写，不适合使用于代码生成场景，使用JPA的 Criteria
         */
        if (!request.getFilters().isEmpty()) {
            Iterator<Filter> filters = request.getFilters().iterator();
            while (filters.hasNext()){
                Filter filter = filters.next();
                QDepartment.department.as(filter.getField());
            }
         //第二种是通过代码生成把查询条件全部列出来。便于修改和查看
        StringFilter filter = (StringFilter) request.getFilters().iterator().next();
        BooleanBuilder bb = new BooleanBuilder();
        if(!StringUtils.isEmpty(filter.getValue())){
            bb.and(QDepartment.department.code.contains(filter.getValue()));
        }

        bb.or(QDepartment.department.name.contains(filter.getValue()));
        bb.or(QDepartment.department.firstName.contains(filter.getValue()));
        bb.or(QDepartment.department.email.contains(filter.getValue()));

        query.where(bb);
        }

        QueryUtil.addPagingAndSorting(query, request, User.class, QUser.user);
        SearchResults<Department> searchResult = query.listResults(QUser.user);

            return new ExtDirectStoreResult<>(searchResult.getTotal(), searchResult.getResults());
            }

            @ExtDirectMethod
            @Transactional
            @PreAuthorize("isAuthenticated()")
            public ExtDirectStoreValidationResult<User> updateSettings(Department modifiedUser, Locale locale) {

                List<ValidationError> validations = Lists.newArrayList();
                    User dbUser = null;
                    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                    if (principal instanceof JpaUserDetails) {
                    dbUser = entityManager.find(User.class, ((JpaUserDetails) principal).getUserDbId());
                    if (dbUser != null) {
                    dbUser.setName(modifiedUser.getName());
                    dbUser.setFirstName(modifiedUser.getFirstName());
                    dbUser.setEmail(modifiedUser.getEmail());
                    dbUser.setLocale(modifiedUser.getLocale());
                    if (StringUtils.hasText(modifiedUser.getPasswordNew())) {
                    if (passwordEncoder.matches(modifiedUser.getOldPassword(), dbUser.getPasswordHash())) {
                    if (modifiedUser.getPasswordNew().equals(modifiedUser.getPasswordNewConfirm())) {
                    dbUser.setPasswordHash(passwordEncoder.encode(modifiedUser.getPasswordNew()));
                    } else {
                    ValidationError error = new ValidationError();
                    error.setField("passwordNew");
                    error.setMessage(messageSource.getMessage("user_passworddonotmatch", null, locale));
                    validations.add(error);
                    }
                    } else {
                    ValidationError error = new ValidationError();
                    error.setField("oldPassword");
                    error.setMessage(messageSource.getMessage("user_wrongpassword", null, locale));
                    validations.add(error);
                    }
                    }
                    }
                    }

                    ExtDirectStoreValidationResult<User> result = new ExtDirectStoreValidationResult<>(dbUser);
                        result.setValidations(validations);
                        return result;
                        }

                        @Override
                        protected List<ValidationError> validateEntity(User entity) {
                            List<ValidationError> validations = super.validateEntity(entity);

                                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                                Locale locale = Locale.ENGLISH;
                                if (principal instanceof JpaUserDetails) {
                                locale = ((JpaUserDetails) principal).getLocale();
                                }

                                if (entity.getPasswordNew() != null && !entity.getPasswordNew().equals(entity.getPasswordNewConfirm())) {
                                ValidationError validationError = new ValidationError();
                                validationError.setField("passwordNew");
                                validationError.setMessage(messageSource.getMessage("user_passworddonotmatch", null, locale));
                                validations.add(validationError);
                                }

                                // Check uniqueness of userName and email
                                if (StringUtils.hasText(entity.getUserName())) {
                                BooleanBuilder bb = new BooleanBuilder(QUser.user.userName.equalsIgnoreCase(entity.getUserName()));
                                if (entity.getId() != null) {
                                bb.and(QUser.user.id.ne(entity.getId()));
                                }
                                if (new JPAQuery(entityManager).from(QUser.user).where(bb).exists()) {
                                ValidationError validationError = new ValidationError();
                                validationError.setField("userName");
                                validationError.setMessage(messageSource.getMessage("user_usernametaken", null, locale));
                                validations.add(validationError);
                                }
                                }

                                if (StringUtils.hasText(entity.getEmail())) {
                                BooleanBuilder bb = new BooleanBuilder(QUser.user.email.equalsIgnoreCase(entity.getEmail()));
                                if (entity.getId() != null) {
                                bb.and(QUser.user.id.ne(entity.getId()));
                                }
                                if (new JPAQuery(entityManager).from(QUser.user).where(bb).exists()) {
                                ValidationError validationError = new ValidationError();
                                validationError.setField("email");
                                validationError.setMessage(messageSource.getMessage("user_emailtaken", null, locale));
                                validations.add(validationError);
                                }
                                }

                                return validations;
                                }

                                @Override
                                protected void preModify(User entity) {
                                super.preModify(entity);

                                if (StringUtils.hasText(entity.getPasswordNew())) {
                                entity.setPasswordHash(passwordEncoder.encode(entity.getPasswordNew()));
                                } else {
                                if (entity.getId() != null) {
                                String dbPassword = new JPAQuery(entityManager).from(QUser.user)
                                .where(QUser.user.id.eq(entity.getId())).singleResult(QUser.user.passwordHash);
                                entity.setPasswordHash(dbPassword);
                                }
                                }
                                }

                                @Override
                                @Transactional
                                public ExtDirectStoreResult<User> destroy(Long id) {
                                    if (!isLastAdmin(id)) {
                                    return super.destroy(id);
                                    }

                                    ExtDirectStoreResult<User> result = new ExtDirectStoreResult<>();
                                        result.setSuccess(false);
                                        return result;
                                        }

                                        private boolean isLastAdmin(Long id) {
                                        JPQLQuery query = new JPAQuery(entityManager).from(QUser.user);
                                        BooleanBuilder bb = new BooleanBuilder();
                                        bb.or(QUser.user.role.eq(Role.ADMIN.name()));
                                        bb.or(QUser.user.role.endsWith("," + Role.ADMIN.name()));
                                        bb.or(QUser.user.role.contains("," + Role.ADMIN.name() + ","));
                                        bb.or(QUser.user.role.startsWith(Role.ADMIN.name() + ","));

                                        query.where(QUser.user.id.ne(id).and(bb));
                                        return query.notExists();
                                        }

                                        @ExtDirectMethod(STORE_READ)
                                        @PreAuthorize("isAuthenticated()")
                                        public List<Map<String, String>> readRoles() {
                                        List<Map<String, String>> result = Lists.newArrayList();
                                        for (Role role : Role.values()) {
                                        result.add(ImmutableMap.of("name", role.name()));
                                        }
                                        return result;
                                        }
                                        }
