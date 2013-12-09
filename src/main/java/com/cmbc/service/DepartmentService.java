package com.cmbc.service;

import ch.ralscha.extdirectspring.annotation.ExtDirectMethod;
import ch.ralscha.extdirectspring.bean.ExtDirectStoreReadRequest;
import ch.ralscha.extdirectspring.bean.ExtDirectStoreResult;
import ch.ralscha.extdirectspring.filter.StringFilter;
import ch.rasc.edsutil.BaseCRUDService;
import ch.rasc.edsutil.QueryUtil;
import ch.rasc.edsutil.bean.ExtDirectStoreValidationResult;
import ch.rasc.edsutil.bean.ValidationError;
import com.cmbc.entity.Department;
import com.cmbc.entity.QDepartment;
import com.cmbc.entity.QUser;
import com.cmbc.entity.User;
import com.google.common.collect.Lists;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.SearchResults;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Locale;

import static ch.ralscha.extdirectspring.annotation.ExtDirectMethodType.STORE_READ;

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

		JPQLQuery query = new JPAQuery(entityManager).from(QUser.user);
		if (!request.getFilters().isEmpty()) {
            BooleanBuilder bb = new BooleanBuilder();
            while (request.getFilters().iterator().hasNext()){
                StringFilter filter = (StringFilter) request.getFilters().iterator().next();
                String fieldName = filter.getField();
                if(StringUtils.isEmpty(filter.getValue())){

                    if(QUser.user.userName.getMetadata().getName().equals(fieldName)){
                        bb.and(QUser.user.userName.contains(filter.getValue()));
                    }else  if(QUser.user.userName.getMetadata().getName().equals(fieldName)){
                        bb.and(QUser.user.userName.contains(filter.getValue()));
                    } if(QUser.user.userName.getMetadata().getName().equals(fieldName)){
                        bb.and(QUser.user.userName.contains(filter.getValue()));
                    }
                }
            }
			query.where(bb);
		}

		QueryUtil.addPagingAndSorting(query, request, User.class, QUser.user);
		SearchResults<Department> searchResult = query.listResults(QDepartment.department);

		return new ExtDirectStoreResult<>(searchResult.getTotal(), searchResult.getResults());
	}

	@ExtDirectMethod
	@Transactional
	@PreAuthorize("isAuthenticated()")
	public ExtDirectStoreValidationResult<Department> updateSettings(Department modifiedUser, Locale locale) {

		List<ValidationError> validations = Lists.newArrayList();
        Department dbUser = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ExtDirectStoreValidationResult<Department> result = new ExtDirectStoreValidationResult<>(dbUser);
		result.setValidations(validations);
		return result;
	}

	@Override
	protected List<ValidationError> validateEntity(Department entity) {
		List<ValidationError> validations = super.validateEntity(entity);

		return validations;
	}

	@Override
	protected void preModify(Department entity) {
		super.preModify(entity);
	}

	@Override
	@Transactional
	public ExtDirectStoreResult<Department> destroy(Long id) {

		ExtDirectStoreResult<Department> result = new ExtDirectStoreResult<>();
		result.setSuccess(false);
		return result;
	}


}
