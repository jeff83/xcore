package com.cmbc.codegenerator;

import com.cmbc.codegenerator.model.ModelBean;

/**
 *
 */
public class TemplateContext {
    private TemplateConfig config;

    private ModelBean model;

    public TemplateContext(TemplateConfig config,ModelBean model){
        this.config = config;
        this.model = model;
    }


    public TemplateConfig getConfig() {
        return config;
    }

    public void setConfig(TemplateConfig config) {
        this.config = config;
    }

    public ModelBean getModel() {
        return model;
    }

    public void setModel(ModelBean model) {
        this.model = model;
    }
}
