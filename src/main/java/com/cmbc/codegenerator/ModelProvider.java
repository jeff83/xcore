package com.cmbc.codegenerator;

import com.cmbc.codegenerator.model.ModelBean;

import java.util.List;

/**
 * 模型对象的提供者.
 * User: jeff
 * Date: 13-12-4
 * Time: 下午4:57
 * To change this template use File | Settings | File Templates.
 */
public interface ModelProvider {

    public List<ModelBean> getModelBeans();

}
