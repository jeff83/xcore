package com.cmbc.codegenerator;

/**
 * 模板配置信息
 * User: Administrator
 * Date: 13-11-28
 * Time: 下午2:06
 * To change this template use File | Settings | File Templates.
 */
public class TemplateConfig {
    /**
     * 模板文件名
     */
    private String  templateFile;
    /**
     * 目标文件名模板
     */
    private String targetFileNameTemplateStr;
    /**
     * 目标文件名目录模板
     */
    private String targetFileDirTemplateStr;
    /**
     * 目标文件名基础目录
     */
    private String targetFileBasePath;

    public TemplateConfig(String  templateFile,String targetFileBasePath,String targetFileDirTemplateStr,String targetFileNameTemplateStr){
        this.setTemplateFile(templateFile);
        this.setTargetFileBasePath(targetFileBasePath);
        this.setTargetFileDirTemplateStr(targetFileDirTemplateStr);
        this.setTargetFileNameTemplateStr(targetFileNameTemplateStr);
    }

    public String getTargetFileBasePath() {
        return targetFileBasePath;
    }

    public void setTargetFileBasePath(String targetFileBasePath) {
        this.targetFileBasePath = targetFileBasePath;
    }

    public String getTemplateFile() {
        return templateFile;
    }

    public void setTemplateFile(String templateFile) {
        this.templateFile = templateFile;
    }

    public String getTargetFileNameTemplateStr() {
        return targetFileNameTemplateStr;
    }

    public void setTargetFileNameTemplateStr(String targetFileNameTemplateStr) {
        this.targetFileNameTemplateStr = targetFileNameTemplateStr;
    }

    public String getTargetFileDirTemplateStr() {
        return targetFileDirTemplateStr;
    }

    public void setTargetFileDirTemplateStr(String targetFileDirTemplateStr) {
        this.targetFileDirTemplateStr = targetFileDirTemplateStr;
    }
}
