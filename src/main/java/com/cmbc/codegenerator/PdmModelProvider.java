package com.cmbc.codegenerator;

import com.cmbc.codegenerator.model.ModelBean;
import com.cmbc.codegenerator.model.ModelFieldBean;
import com.cmbc.codegenerator.model.ModelFieldType;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


import java.io.*;
import java.util.*;

/**
 * 根据PDM文件生成ModelBean
 * User: jeff
 * Date: 13-11-25
 * Time: 下午10:52
 * To change this template use File | Settings | File Templates.
 */
public class PdmModelProvider implements ModelProvider{

    Logger logger = (Logger) LogManager.getLogger(PdmModelProvider.class);

    private String pdmFilePath;

    /**
     * 如果设置该属性，仅生成该属性的文件，不设置时，默认对PDM中的所有表进行处理
     */
    private String TableNameForGen;


    public void setPdmFilePath(String pdmFilePath) {
        this.pdmFilePath = pdmFilePath;
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

    public Map<String,ModelBean> parsePDM(String filePath) {
        Map<String,ModelBean> modelBeansMap = new HashMap<String,ModelBean>();
        ModelBean modelBean = null;
        ModelFieldBean[] fieldBeans = null;
        File f = new File(filePath);
        SAXReader sr = new SAXReader();
        Document doc = null;
        try {
            doc = sr.read(f);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Iterator itr = doc.selectNodes("//c:Tables//o:Table").iterator();
        while (itr.hasNext()) {
            modelBean = new ModelBean();
            fieldBeans = new ModelFieldBean[] {};
            List<ModelFieldBean> list = new ArrayList<ModelFieldBean>();
            ModelFieldBean col = null;
            Element e_table = (Element) itr.next();
            modelBean.setLabel(e_table.elementTextTrim("Name"));
            String tableName  = e_table.elementTextTrim("Code");
            modelBean.setTableName(tableName);

            String className = getClassName(tableName);
            modelBean.setClassName(className);
            Iterator itr1 = e_table.element("Columns").elements("Column").iterator();
            while (itr1.hasNext()) {
                col = new ModelFieldBean();
                Element e_col = (Element) itr1.next();
                try {
                    String pkID = e_col.attributeValue("Id");
                    col.setDefaultValue(e_col.elementTextTrim("DefaultValue"));
                    col.setLabel(e_col.elementTextTrim("Name"));
                    col.setName(getFieldNameByDbCode(e_col.elementTextTrim("Code")));
                    String dataType = e_col.elementTextTrim("DataType");

                    ModelFieldType  modelFieldType = ModelFieldType.getByDateType(dataType);
                    col.setType(modelFieldType);
                    col.setMax(e_col.elementTextTrim("Length") == null ? null : Integer.parseInt(e_col.elementTextTrim("Length")));
                    if(e_table.element("Keys")!=null){
                        String keys_key_id = e_table.element("Keys").element("Key").attributeValue("Id");
                        String keys_column_ref = e_table.element("Keys").element("Key").element("Key.Columns")
                                .element("Column").attributeValue("Ref");
                        String keys_primarykey_ref_id = e_table.element("PrimaryKey").element("Key").attributeValue("Ref");

                        if (keys_primarykey_ref_id.equals(keys_key_id) && keys_column_ref.equals(pkID)) {
                            col.setPkFlag(true);
                            modelBean.setIdProperty(col.getName());
                        }

                    }
                    list.add(col);
                    System.out.println(col);
                } catch (Exception ex) {
                    System.out.println("+++++++++有错误++++" );
                    ex.printStackTrace();
                }
            }
            modelBean.addFields(list);
            modelBeansMap.put(modelBean.getTableName(), modelBean);
            System.out.println(modelBean);
            System.out.println("======================");
            System.out.println();
        }
        return modelBeansMap;
    }

    public String getFieldNameByDbCode(String code){
        String[] items = code.split("\\_");
        String fieldName=items[0];
        for (int i=1;i<items.length;i++){
            fieldName = fieldName+items[i].substring(0,1).toUpperCase()+items[i].substring(1);
        }
        return fieldName;
    }

    private String getClassName(String tableName) {
        String className = "";
        tableName = tableName.toLowerCase();
        String[] tableNamePrefixs = Config.getInstance().getTableNamePrefixs();
        for (int i=0;i<tableNamePrefixs.length;i++){
            String tableNamePrefix = tableNamePrefixs[i].toLowerCase();
            if(tableName.startsWith(tableNamePrefix)){
                tableName = tableNamePrefix.replace(tableNamePrefix,"");
            }
        }
        String[] tableNameItems = tableName.split("\\_");
        for (int i=0;i<tableNameItems.length;i++){
            String classNameItem = tableNameItems[i].substring(0,1).toUpperCase()+tableNameItems[i].substring(1);
            className+=classNameItem;
        }
        return className;
    }

    @Override
    public List<ModelBean> getModelBeans() {
        Map<String,ModelBean> modelBeanMap = this.parsePDM(this.pdmFilePath);
        String tableName =this.getTableNameForGen();
        List<ModelBean> modelBeans = new ArrayList<>();
        if(StringUtils.isNotBlank(tableName)){
            modelBeans.add(modelBeanMap.get(tableName));
        }else {
            modelBeans.addAll(modelBeanMap.values());
        }
        return modelBeans;
    }
}
