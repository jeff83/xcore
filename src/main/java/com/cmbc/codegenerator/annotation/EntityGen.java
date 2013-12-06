package com.cmbc.codegenerator.annotation;

import java.lang.annotation.*;

/**
 * Annotation to configure different aspects of a model object
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface EntityGen {
    /**
     * 实体的显示名，用于title等显示位置
     * @return
     */
    String label() default "";

}
