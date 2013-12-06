package com.cmbc.codegenerator;


import ch.ralscha.extdirectspring.generator.ModelType;
import com.cmbc.codegenerator.annotation.EntityGen;

import com.cmbc.codegenerator.annotation.FieldGen;
import com.cmbc.codegenerator.annotation.InputType;
import com.cmbc.codegenerator.model.ModelBean;
import com.cmbc.codegenerator.model.ModelFieldBean;
import com.cmbc.entity.Department;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.Id;
import javax.validation.constraints.Size;
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





        final ModelBean model = new ModelBean();

        model.setClassName(entity.getSimpleName());
        model.setImportName(entity.getName());
        model.setPackageName(entity.getPackage().getName());
        model.setDefaultInstance(entity.getSimpleName().substring(0,1).toLowerCase()+entity.getSimpleName().substring(1));


        EntityGen entityGenAnnotation = entity.getAnnotation(EntityGen.class);
        if (entityGenAnnotation != null && StringUtils.hasText(entityGenAnnotation.label())) {
            model.setLabel(entityGenAnnotation.label());
        } else {
            model.setLabel(entity.getSimpleName());
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

        final List<ModelFieldBean> modelFields = new ArrayList<ModelFieldBean>();

        ReflectionUtils.doWithFields(entity, new ReflectionUtils.FieldCallback() {
            private final Set<String> fields = new HashSet<String>();

            @Override
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                if (fields.contains(field.getName())) return;
                //只有配置了FiledGen的属性才能生成界面元素
                if (field.getAnnotation(FieldGen.class) != null || field.getAnnotation(Id.class) != null ) {
                    // ignore superclass declarations of fields already found in
                    // a subclass
                    fields.add(field.getName());

                    ModelFieldBean modelFieldBean = new ModelFieldBean();

                    modelFieldBean.setName(field.getName());

                    //@FieldGen
                    FieldGen fieldGenAnnotation = field.getAnnotation(FieldGen.class);
                    if(fieldGenAnnotation != null){
                        modelFieldBean.setLabel(fieldGenAnnotation.label());
                        //输入框类型
                        if (fieldGenAnnotation.inputType()!=null){
                            modelFieldBean.setInputType(fieldGenAnnotation.inputType());
                        }else {
                            Class<?> javaType = field.getType();
                            modelFieldBean.setInputType(InputType.getDefaultInputType(javaType));
                        }
                        if (modelFieldBean.getInputType().equals(InputType.DATE)){
                            if(fieldGenAnnotation.dateFormat()!=null){
                                modelFieldBean.setDateFormat(fieldGenAnnotation.dateFormat());
                            }else {
                                modelFieldBean.setDateFormat(Config.getInstance().getDefaultDateFormat());
                            }
                        }
                    }

                    //@Size
                    Size size = field.getAnnotation(Size.class);
                   if(size!=null && size.max()!=2147483647){
                       modelFieldBean.setMax(size.max());
                   }
                    if(size!=null && size.min()!=0){
                        modelFieldBean.setMin(size.min());
                    }

                     modelFields.add(modelFieldBean);
                }
            }

        });

        model.addFields(modelFields);
        return model;
    }

    public Class<?> getEntity() {
        return entity;
    }

    public void setEntity(Class<?> entity) {
        this.entity = entity;
    }
    public static void main(String[] args){
        System.out.println(Department.class.getCanonicalName());
        System.out.println(Department.class.getName());
        System.out.println(Department.class.getSimpleName());
        System.out.println(Department.class.getPackage().getName());
    }
}
