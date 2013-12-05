package com.cmbc.codegenerator;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-12-2
 * Time: 下午3:36
 * To change this template use File | Settings | File Templates.
 */
public class GeneratorException extends RuntimeException{
    public GeneratorException(Exception e) {
        super(e);
    }
    public GeneratorException(String msg) {
        super(msg);
    }
}
