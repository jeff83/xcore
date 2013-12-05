package com.cmbc.codegenerator;



import ch.ralscha.extdirectspring.generator.*;
import ch.ralscha.extdirectspring.generator.association.AbstractAssociation;
import com.cmbc.codegenerator.freemarker.FreemarkerHelper;
import com.cmbc.codegenerator.model.*;
import com.cmbc.codegenerator.model.ModelBean;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 根据entity实体类，或者powerdesigner或者excel生成文件。.
 * 使用entity更灵活，生成数据表，界面等都很方便
 * User: jeff
 * Date: 13-11-14
 * Time: 下午12:37
 * To change this template use File | Settings | File Templates.
 */
public class Generator {

    Logger logger = (Logger) LogManager.getLogger(Generator.class);

    public static void main(String[] args) throws GeneratorException {
        Generator generator = new Generator();
        generator.generate();
    }

    public void generate(){
        ModelProvider modelProvider = Config.getInstance().getModelProvider();
        List<ModelBean> modelBeans = modelProvider.getModelBeans();
        for(ModelBean modelBean:modelBeans){
            this.render(modelBean);
        }
    }

    public void render(ModelBean modelBean) throws GeneratorException {
        List<TemplateConfig> templateConfigs = Config.getInstance().getTemplateConfigs();
        for (TemplateConfig templateConfig:templateConfigs){
            TemplateContext templateContext = new TemplateContext(templateConfig,modelBean);
            String targetFileName = FreemarkerHelper.processTemplateString(templateConfig.getTargetFileNameTemplateStr(), templateContext, new Configuration());
            String targetFileDir = FreemarkerHelper.processTemplateString(templateConfig.getTargetFileDirTemplateStr(), templateContext, new Configuration());
            genFile(templateConfig.getTemplateFile(), templateContext, templateConfig.getTargetFileBasePath() + targetFileDir, targetFileName);
        }
    }


    public boolean genFile(String templateFileName, Object propMap,
                           String targetFilePath, String targetFileName) {
        try {
            Template template = Config.getInstance().getFreemarkerCfg().getTemplate(templateFileName);

            // 如果根路径存在,则递归创建子目录
            FileUtils.forceMkdir(new File(targetFilePath));

            File targetFile = new File(targetFilePath + "/" + targetFileName);

            if(targetFile.exists()){
                throw new GeneratorException("目标文件已经存在:["+targetFile.getPath()+"]");
            }

            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFile)));

            template.process(propMap, out);
        } catch (TemplateException e) {
            logger.error("Error while processing FreeMarker template "
                    + templateFileName, e);
            return false;
        } catch (IOException e) {
            logger.error("Error while generate Static Html File "
                    + targetFileName, e);
            return false;
        }

        return true;
    }

}
