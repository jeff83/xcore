package com.cmbc.codegenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jeff
 * Date: 13-11-25
 * Time: 下午10:51
 * To change this template use File | Settings | File Templates.
 */
public class Config {

    private String pdmFilePath;

    private static  Config instance;

    private String projectName;

    /**
     * 如果设置该属性，仅生成该属性的文件，不设置时，默认对PDM中的所有表进行处理
     */
    private String TableNameForGen;

    private List<TemplateConfig> templateConfigs = new ArrayList<TemplateConfig>();

    private String templatePath = "";

    public static Config getInstance(){
        if(instance==null){
            instance=new Config();
            instance.setPdmFilePath("D:\\workspace\\param.pdm");
            instance.setProjectName("xcore");
            instance.setTableNameForGen("");
            instance.setTemplatePath(Config.class.getClassLoader().getResource("com.cmbc.codegenerator.template").getPath());
            String jsBasePath = "D:\\workspace\\eds\\xcore\\src\\main\\webapp\\app\\";
            String javaBasePath = "D:\\workspace\\eds\\xcore\\src\\main\\java\\";
            instance.getTemplateConfigs().add(new TemplateConfig("controller.js.ftl",jsBasePath,"controller\\","${className}.js"));
            instance.getTemplateConfigs().add(new TemplateConfig("Edit.js.ftl",jsBasePath,"view\\${lowerClassName}\\","Edit.js"));
            instance.getTemplateConfigs().add(new TemplateConfig("List.js.ftl",jsBasePath,"view\\${lowerClassName}\\","List.js"));
            instance.getTemplateConfigs().add(new TemplateConfig("store.js.ftl",jsBasePath,"store\\","${className}s.js"));
            instance.getTemplateConfigs().add(new TemplateConfig("service.java.ftl",javaBasePath,"com\\cmbc\\service\\","${className}Service.java"));
        }
        return instance;
    }

    public String getPdmFilePath() {
        return pdmFilePath;
    }

    public void setPdmFilePath(String pdmFilePath) {
        this.pdmFilePath = pdmFilePath;
    }

    /**
     * ExtJS mvc中需要
     */
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public List<TemplateConfig> getTemplateConfigs() {
        return templateConfigs;
    }

    public void setTemplateConfigs(List<TemplateConfig> templateConfigs) {
        this.templateConfigs = templateConfigs;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    /**
     * 要生成文件的表名
     */
    public String getTableNameForGen() {
        return TableNameForGen;
    }

    public void setTableNameForGen(String tableNameForGen) {
        TableNameForGen = tableNameForGen;
    }
}
