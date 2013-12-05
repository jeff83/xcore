package com.cmbc.codegenerator;

import ch.ralscha.extdirectspring.generator.*;
import ch.ralscha.extdirectspring.generator.association.AbstractAssociation;
import com.cmbc.codegenerator.model.ModelBean;
import com.cmbc.codegenerator.model.ModelFieldBean;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: jeff
 * Date: 13-11-25
 * Time: 下午10:52
 * To change this template use File | Settings | File Templates.
 */
public class EntityModelProvider implements ModelProvider {
    @Override
    public List<ModelBean> getModelBeans() {
        List<ModelBean> modelBeans = new ArrayList<>(1);
        modelBeans.add(createModel(getEntity()));
        return modelBeans;
    }

    private Class<?> entity;

    public ModelBean createModel(Class<?> entity) {

        Assert.notNull(entity, "entity must not be null");

        Model modelAnnotation = entity.getAnnotation(Model.class);

        final ModelBean model = new ModelBean();

        //获取extjs的模型对象名
        if (modelAnnotation != null && StringUtils.hasText(modelAnnotation.value())) {
            model.setName(modelAnnotation.value());
        } else {
            model.setName(entity.getName());
        }
        entity.getName()

        if (modelAnnotation != null) {
            model.setIdProperty(modelAnnotation.idProperty());
            model.setPaging(modelAnnotation.paging());
            model.setDisablePagingParameters(modelAnnotation.disablePagingParameters());

            if (StringUtils.hasText(modelAnnotation.createMethod())) {
                model.setCreateMethod(modelAnnotation.createMethod());
            }

            if (StringUtils.hasText(modelAnnotation.readMethod())) {
                model.setReadMethod(modelAnnotation.readMethod());
            }

            if (StringUtils.hasText(modelAnnotation.updateMethod())) {
                model.setUpdateMethod(modelAnnotation.updateMethod());
            }

            if (StringUtils.hasText(modelAnnotation.destroyMethod())) {
                model.setDestroyMethod(modelAnnotation.destroyMethod());
            }

            if (StringUtils.hasText(modelAnnotation.messageProperty())) {
                model.setMessageProperty(modelAnnotation.messageProperty());
            }
        }
        //可读属性名称列表
        final Set<String> hasReadMethod = new HashSet<String>();

        BeanInfo bi;
        try {
            bi = Introspector.getBeanInfo(entity);
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }

        for (PropertyDescriptor pd : bi.getPropertyDescriptors()) {
            if (pd.getReadMethod() != null && pd.getReadMethod().getAnnotation(JsonIgnore.class) == null) {
                hasReadMethod.add(pd.getName());
            }
        }

        final List<com.cmbc.codegenerator.model.ModelFieldBean> modelFields = new ArrayList<ModelFieldBean>();
        final List<AbstractAssociation> associations = new ArrayList<AbstractAssociation>();

        ReflectionUtils.doWithFields(entity, new ReflectionUtils.FieldCallback() {
            private final Set<String> fields = new HashSet<String>();

            @Override
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                if (fields.contains(field.getName())) return;
                if (field.getAnnotation(ModelField.class) != null
                        || field.getAnnotation(ModelAssociation.class) != null ||
                        ((Modifier.isPublic(field.getModifiers()) || hasReadMethod.contains(field.getName()))
                                && field.getAnnotation(JsonIgnore.class) == null)) {

                    // ignore superclass declarations of fields already found in
                    // a subclass
                    fields.add(field.getName());

                    Class<?> javaType = field.getType();

                    ModelType modelType = null;
                    for (ModelType mt : ModelType.values()) {
                        if (mt.supports(javaType)) {
                            modelType = mt;
                            break;
                        }
                    }

                    com.cmbc.codegenerator.model.ModelFieldBean modelFieldBean = null;

                    ModelField modelFieldAnnotation = field.getAnnotation(ModelField.class);
                    if (modelFieldAnnotation != null) {

                        String name;
                        if (StringUtils.hasText(modelFieldAnnotation.value())) {
                            name = modelFieldAnnotation.value();
                        } else {
                            name = field.getName();
                        }

                        ModelType type;
                        if (modelFieldAnnotation.type() != ModelType.AUTO) {
                            type = modelFieldAnnotation.type();
                        } else {
                            if (modelType != null) {
                                type = modelType;
                            } else {
                                type = ModelType.AUTO;
                            }
                        }

                        modelFieldBean = new com.cmbc.codegenerator.model.ModelFieldBean(name, type);

                        if (StringUtils.hasText(modelFieldAnnotation.dateFormat()) && type == ModelType.DATE) {
                            modelFieldBean.setDateFormat(modelFieldAnnotation.dateFormat());
                        }

                        String defaultValue = modelFieldAnnotation.defaultValue();
                        if (StringUtils.hasText(defaultValue)) {
                            if (ModelField.DEFAULTVALUE_UNDEFINED.equals(defaultValue)) {
                                modelFieldBean.setDefaultValue(ModelField.DEFAULTVALUE_UNDEFINED);
                            } else {
                                if (type == ModelType.BOOLEAN) {
                                    modelFieldBean.setDefaultValue(Boolean.parseBoolean(defaultValue));
                                } else if (type == ModelType.INTEGER) {
                                    modelFieldBean.setDefaultValue(Long.valueOf(defaultValue));
                                } else if (type == ModelType.FLOAT) {
                                    modelFieldBean.setDefaultValue(Double.valueOf(defaultValue));
                                } else {
                                    modelFieldBean.setDefaultValue("\"" + defaultValue + "\"");
                                }
                            }
                        }

                        if (modelFieldAnnotation.useNull()
                                && (type == ModelType.INTEGER || type == ModelType.FLOAT || type == ModelType.STRING || type == ModelType.BOOLEAN)) {
                            modelFieldBean.setUseNull(true);
                        }

                        if (StringUtils.hasText(modelFieldAnnotation.mapping())) {
                            modelFieldBean.setMapping(modelFieldAnnotation.mapping());
                        }

                        if (!modelFieldAnnotation.persist()) {
                            modelFieldBean.setPersist(modelFieldAnnotation.persist());
                        }

                        if (StringUtils.hasText(modelFieldAnnotation.convert())) {
                            modelFieldBean.setConvert(modelFieldAnnotation.convert());
                        }

                        modelFields.add(modelFieldBean);
                    } else {
                        if (modelType != null) {
                            modelFieldBean = new com.cmbc.codegenerator.model.ModelFieldBean(field.getName(), modelType);
                            modelFields.add(modelFieldBean);
                        }
                    }

                  /*  ModelAssociation modelAssociationAnnotation = field.getAnnotation(ModelAssociation.class);
                    if (modelAssociationAnnotation != null) {
                        associations.add(AbstractAssociation
                                .createAssociation(modelAssociationAnnotation, model, field));
                    }

                    if (modelFieldBean != null && outputConfig.getIncludeValidation() != IncludeValidation.NONE) {
                        Annotation[] fieldAnnotations = field.getAnnotations();

                        for (Annotation fieldAnnotation : fieldAnnotations) {
                            AbstractValidation.addValidationToModel(model, modelFieldBean, fieldAnnotation,
                                    outputConfig.getIncludeValidation());
                        }
                    }*/

                }
            }

        });

        model.addFields(modelFields);
        model.addAssociations(associations);
        return model;
    }

    public Class<?> getEntity() {
        return entity;
    }

    public void setEntity(Class<?> entity) {
        this.entity = entity;
    }
}
