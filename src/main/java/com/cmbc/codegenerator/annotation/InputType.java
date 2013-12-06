package com.cmbc.codegenerator.annotation;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 输入框类型
 * User: jeff
 * Date: 13-12-5
 * Time: 上午10:59
 * To change this template use File | Settings | File Templates.
 */
public enum InputType {

    /** 文本输入框. */
    TEXT("text"),

    /** 选择框. */
    SELECT("select"),

    /**日历控制*/
    DATE("date"),

    /**数字输入框  */
    INT("int"),

    FLOAT("float");

    private String type;

    private InputType(String type) {
        this.type = type;
    }

    public String toString() {
        return type;
    }
    /**
     * @return the name of the type for JS code
     */
    public String getType() {
        return type;
    }

    private static Map<Class<?>,InputType> typeMap = null;
    public static InputType getDefaultInputType(Class<?> type){
        if(typeMap==null){
            typeMap.put(String.class,TEXT);

            typeMap.put(Date.class,DATE);
            typeMap.put(java.sql.Date.class,DATE);
            typeMap.put(Timestamp.class,DATE);

            typeMap.put(Byte.class,INT);
            typeMap.put(Short.class,INT);
            typeMap.put(Integer.class,INT);
            typeMap.put(Long.class,INT);
            typeMap.put(BigInteger.class,INT);
            typeMap.put(Byte.TYPE,INT);
            typeMap.put(Short.TYPE,INT);
            typeMap.put(Integer.TYPE,INT);
            typeMap.put(Long.TYPE,INT);

            typeMap.put(Float.class,FLOAT);
            typeMap.put(Double.class,FLOAT);
            typeMap.put(BigDecimal.class,FLOAT);
            typeMap.put(Float.TYPE,FLOAT);
            typeMap.put(Double.TYPE,FLOAT);
        }
        InputType  defaultInputType = typeMap.get(type);
        if (defaultInputType==null){
            defaultInputType = TEXT;
        }
        return defaultInputType;
    }
}