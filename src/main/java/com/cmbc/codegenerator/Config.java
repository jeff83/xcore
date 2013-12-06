package com.cmbc.codegenerator;

import com.cmbc.entity.Department;
import freemarker.template.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 *生成器配置
 * User: jeff
 * Date: 13-11-25
 * Time: 下午10:51
 * To change this template use File | Settings | File Templates.
 */
public class Config {

    private static  Config instance;

    private String projectName;

    private List<TemplateConfig> templateConfigs = new ArrayList<TemplateConfig>();

    private String templatePath = "";

    private Configuration freemarkerCfg = null;

    private String[] tableNamePrefixs = new String[]{"rbac_","wf_"};



    private ModelProvider modelProvider;

    private String defaultEncoding = "UTF-8";

    /**
     * 该默认值 与@FieldGen中的dateFormat有影响
     */
    private String defaultDateFormat = "yyyy-MM-dd";

    public static Config getInstance(){
        if(instance==null){
            //设置config的配置项
            instance=new Config();
            instance.setProjectName("xcore");


            instance.setModelProvider(getTestEntityModelProvider());
            //instance.setModelProvider(getTestPdmModelProvider());


            String jsBasePath = "./src/main/webapp/app/";
            String javaBasePath = "./src/main/java/";
//            instance.getTemplateConfigs().add(new TemplateConfig("controller.js.ftl",jsBasePath,"controller/","${className}.js"));
//            instance.getTemplateConfigs().add(new TemplateConfig("Edit.js.ftl",jsBasePath,"view/${lowerClassName}/","Edit.js"));
//            instance.getTemplateConfigs().add(new TemplateConfig("List.js.ftl",jsBasePath,"view/${lowerClassName}/","List.js"));
//            instance.getTemplateConfigs().add(new TemplateConfig("store.js.ftl",jsBasePath,"store/","${className}s.js"));
            instance.getTemplateConfigs().add(new TemplateConfig("service.java.ftl",javaBasePath,"com/cmbc/service/","${model.className}Service.java"));
            instance.init();
        }
        return instance;
    }

    private static ModelProvider getTestEntityModelProvider() {
        EntityModelProvider entityModelProvider=  new EntityModelProvider();
        entityModelProvider.setEntity(Department.class);
        return entityModelProvider;
    }

    private static ModelProvider getTestPdmModelProvider() {
        PdmModelProvider pdmModelProvider=  new PdmModelProvider();
        pdmModelProvider.setPdmFilePath("./doc/test.pdm");
        pdmModelProvider.setTableNameForGen("");
        return pdmModelProvider;
    }


    /**
     * config的所有配置项配置完成后，进行config的初始化
     */
    public void init(){
        // Initialize the FreeMarker configuration;
        // - Create a configuration instance
        // - FreeMarker支持多种模板装载方式,可以查看API文档,都很简单:路径,根据Servlet上下文,classpath等等
        Configuration freemarkerCfg1 = new Configuration();
        freemarkerCfg1.setClassForTemplateLoading(Config.class, "/com/cmbc/codegenerator/template");
        freemarkerCfg1.setLocalizedLookup(false);//禁止本地化模板文件查找,否则需要将文件名+ZH_CN之类的本地化后缀
        freemarkerCfg1.setBooleanFormat("true,false");
        freemarkerCfg1.setDefaultEncoding(getDefaultEncoding());
        this.setFreemarkerCfg(freemarkerCfg1);
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


    public Configuration getFreemarkerCfg() {
        return freemarkerCfg;
    }

    public void setFreemarkerCfg(Configuration freemarkerCfg) {
        this.freemarkerCfg = freemarkerCfg;
    }

    public String[] getTableNamePrefixs() {
        return tableNamePrefixs;
    }

    public void setTableNamePrefixs(String[] tableNamePrefixs) {
        this.tableNamePrefixs = tableNamePrefixs;
    }

    public String getDefaultEncoding() {
        return defaultEncoding;
    }

    public void setDefaultEncoding(String defaultEncoding) {
        this.defaultEncoding = defaultEncoding;
    }

    public ModelProvider getModelProvider() {
        return modelProvider;
    }

    public void setModelProvider(ModelProvider modelProvider) {
        this.modelProvider = modelProvider;
    }

    public String getDefaultDateFormat() {
        return defaultDateFormat;
    }

    public void setDefaultDateFormat(String defaultDateFormat) {
        this.defaultDateFormat = defaultDateFormat;
    }
}
