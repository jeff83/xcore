package com.cmbc.codegenerator;

import com.cmbc.codegenerator.model.ModelBean;
import com.cmbc.codegenerator.model.ModelFieldBean;
import com.cmbc.codegenerator.model.ModelFieldType;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: jeff
 * Date: 13-11-25
 * Time: 下午10:52
 * To change this template use File | Settings | File Templates.
 */
public class PdmGenerator {

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
            modelBean.setName(e_table.elementTextTrim("Name"));
            modelBean.setTableName(e_table.elementTextTrim("Code"));
            Iterator itr1 = e_table.element("Columns").elements("Column").iterator();
            while (itr1.hasNext()) {
                col = new ModelFieldBean();
                Element e_col = (Element) itr1.next();
                try {


                    String pkID = e_col.attributeValue("Id");
                    col.setDefaultValue(e_col.elementTextTrim("DefaultValue"));
                    col.setName(e_col.elementTextTrim("Name"));
                    col.setFieldNameByDbCode(e_col.elementTextTrim("Code"));
                    String dataType = e_col.elementTextTrim("DataType");

                    ModelFieldType  modelFieldType = ModelFieldType.getByDateType(dataType);
                    col.setType(modelFieldType);
                    col.setLength(e_col.elementTextTrim("Length") == null ? null : Integer.parseInt(e_col.elementTextTrim("Length")));
                    if(e_table.element("Keys")!=null){
                        String keys_key_id = e_table.element("Keys").element("Key").attributeValue("Id");
                        String keys_column_ref = e_table.element("Keys").element("Key").element("Key.Columns")
                                .element("Column").attributeValue("Ref");
                        String keys_primarykey_ref_id = e_table.element("PrimaryKey").element("Key").attributeValue("Ref");

                        if (keys_primarykey_ref_id.equals(keys_key_id) && keys_column_ref.equals(pkID)) {
                            col.setPkFlag(true);
                            modelBean.setIdProperty(col.getCode());
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

    public static void main(String[] args) {
        PdmGenerator pp = new PdmGenerator();
        Map<String,ModelBean> modelBeanMap = pp.parsePDM(Config.getInstance().getPdmFilePath());
        String tableName = Config.getInstance().getTableNameForGen();
        List<ModelBean> modelBeans = new ArrayList<>();
        if(StringUtils.isNotBlank(tableName)){
            modelBeans.add(modelBeanMap.get(tableName));
        }else {
            modelBeans.addAll(modelBeanMap.values());
        }
        for(ModelBean modelBean:modelBeans){
            pp.render(modelBean);
        }

    }
    public void render(ModelBean modelBean){
        List<TemplateConfig> templateConfigs = Config.getInstance().getTemplateConfigs();
        for (TemplateConfig templateConfig:templateConfigs){

        }
    }

}
