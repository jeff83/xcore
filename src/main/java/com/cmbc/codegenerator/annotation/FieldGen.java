package com.cmbc.codegenerator.annotation;

import javax.persistence.FetchType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static javax.persistence.FetchType.LAZY;

/**
 *在Entity实体模型模式下，如果配置@Field属性将作为编辑和DataGrid中的展示列
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface FieldGen {
    /**
     * 字段的展示名
     * @return
     */
      String label() default "";

    /**
     * 输入框类型
     * @return
     */
      InputType inputType() default InputType.TEXT;

    /**
     * 是否可查询
     * @return
     */
    boolean searchable() default false;

    /**
     * 是否必录
     * @return
     */
    boolean required() default false;

    String dateFormat() ;

}
