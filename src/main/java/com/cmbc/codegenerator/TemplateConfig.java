package com.cmbc.codegenerator;

/**
 * 模板配置信息
 * User: Administrator
 * Date: 13-11-28
 * Time: 下午2:06
 * To change this template use File | Settings | File Templates.
 */
public class TemplateConfig {
    private String  templateFile;
    private String fileNameTmpl;
    private String outpathTmpl ;
    private String basePath;

    public TemplateConfig(String  templateFile,String basePath,String outpathTmpl,String fileNameTmpl){
        this.setTemplateFile(templateFile);
        this.setBasePath(basePath);
        this.setOutpathTmpl(outpathTmpl);
        this.setFileNameTmpl(fileNameTmpl);
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getTemplateFile() {
        return templateFile;
    }

    public void setTemplateFile(String templateFile) {
        this.templateFile = templateFile;
    }

    public String getFileNameTmpl() {
        return fileNameTmpl;
    }

    public void setFileNameTmpl(String fileNameTmpl) {
        this.fileNameTmpl = fileNameTmpl;
    }

    public String getOutpathTmpl() {
        return outpathTmpl;
    }

    public void setOutpathTmpl(String outpathTmpl) {
        this.outpathTmpl = outpathTmpl;
    }
}
