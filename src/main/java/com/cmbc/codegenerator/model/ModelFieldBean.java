/**
 * Copyright 2010-2013 Ralph Schaer <ralphschaer@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cmbc.codegenerator.model;

import ch.ralscha.extdirectspring.generator.ModelType;
import com.cmbc.codegenerator.annotation.InputType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.IOException;

/**
 * Represents one field in a {@link ch.ralscha.extdirectspring.generator.ModelBean}
 */
@JsonInclude(Include.NON_NULL)
public class ModelFieldBean {


    private String label;


    /**
     * entity实体类属性名，需要去掉"_",换成camel写法
     *  property
     */
    private String name;


    private boolean pkFlag;

    /**
     * 界面前端输入框的类型
     */
    private InputType inputType;

    /**
     * 转成java的类型
     */
	private ModelFieldType type;

    private Integer max;

    private Integer min;



	@JsonRawValue
	private Object defaultValue;

	private String dateFormat;

	private Boolean useNull;

	private String mapping;

	// only a false value will be generated
	private Boolean persist = null;

	@JsonRawValue
	private String convert;

    /**
     * Creates a new ModelFieldBean
     *
     */
    public ModelFieldBean() {
    }


	public String getName() {
		return name;
	}

	/**
	 * Name of the field. Property '<a
	 * href="http://docs.sencha.com/ext-js/4-2/#!/api/Ext.data.Field-cfg-name"
	 * >name</a>' in JS.
	 *
	 * @param name new name for the field
	 */
	public void setName(String name) {
		this.name = name;
	}

	@JsonSerialize(using = ModelTypeSerializer.class)
	public ModelFieldType getType() {
		return type;
	}

	/**
	 * Type of the field. Property '<a
	 * href="http://docs.sencha.com/ext-js/4-2/#!/api/Ext.data.Field-cfg-type"
	 * >type</a>' in JS.
	 *
	 * @param type new type for the field
	 */
	public void setType(ModelFieldType type) {
		this.type = type;
	}

	public Object getDefaultValue() {
		return defaultValue;
	}

	/**
	 * The default value. Property '<a href=
	 * "http://docs.sencha.com/ext-js/4-2/#!/api/Ext.data.Field-cfg-defaultValue"
	 * >defaultValue</a>' in JS.
	 *
	 * @param defaultValue new defaultValue
	 */
	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	/**
	 * Specifies format of date. Property '<a href=
	 * "http://docs.sencha.com/ext-js/4-2/#!/api/Ext.data.Field-cfg-dateFormat"
	 * >dateFormat</a>' in JS.<br>
	 * For a list of all supported formats see Sencha Doc: <a
	 * href="http://docs.sencha.com/ext-js/4-2/#!/api/Ext.Date">Ext.Date</a>
	 * <p>
	 * Will be ignored if the field is not a {@link ch.ralscha.extdirectspring.generator.ModelType#DATE} field.
	 *
	 * @param dateFormat new dateFormat String
	 */
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public Boolean getUseNull() {
		return useNull;
	}

	/**
	 * If true null value is used if value cannot be parsed. If false default
	 * values are used (0 for integer and float, "" for string and false for
	 * boolean).<br>
	 * Property '<a href=
	 * "http://docs.sencha.com/ext-js/4-2/#!/api/Ext.data.Field-cfg-useNull"
	 * >useNull</a>' in JS.<br>
	 * <p>
	 * Only used if type of field is {@link ch.ralscha.extdirectspring.generator.ModelType#INTEGER},
	 * {@link ch.ralscha.extdirectspring.generator.ModelType#FLOAT}, {@link ch.ralscha.extdirectspring.generator.ModelType#STRING} or
	 * {@link ch.ralscha.extdirectspring.generator.ModelType#BOOLEAN}.
	 * 
	 * @param useNull new value for useNull
	 */
	public void setUseNull(Boolean useNull) {
		this.useNull = useNull;
	}

	public String getMapping() {
		return mapping;
	}

	/**
	 * Typical use for a virtual field to extract field data from the model
	 * object <br>
	 * Property '<a href=
	 * "http://docs.sencha.com/ext-js/4-2/#!/api/Ext.data.Field-cfg-mapping"
	 * >mapping</a>' in JS.<br>
	 * <p>
	 * 
	 * @param mapping A path expression
	 */
	public void setMapping(String mapping) {
		this.mapping = mapping;
	}

	public Boolean getPersist() {
		return persist;
	}

	/**
	 * Prevent the value of this field to be serialized or written with
	 * Ext.data.writer.Writer <br>
	 * Typical use for a virtual field <br>
	 * Property '<a href=
	 * "http://docs.sencha.com/ext-js/4-2/#!/api/Ext.data.Field-cfg-persist"
	 * >persist</a>' in JS.<br>
	 * <p>
	 * 
	 * @param persist defaults to true, only a false value will be generated
	 */
	public void setPersist(Boolean persist) {
		this.persist = persist;
	}

	public String getConvert() {
		return convert;
	}

	/**
	 * function which coerces string values in raw data into the field's type <br>
	 * Typical use for a virtual field <br>
	 * http://localhost/ext4.1/docs/index.html#!/api/Ext.data.Field-cfg-convert
	 * Property '<a href=
	 * "http://docs.sencha.com/ext-js/4-2/#!/api/Ext.data.Field-cfg-convert" >
	 * Ext.data.Field.convert</a>' in JS.<br>
	 * 
	 * @param convert A function. JavaScript Syntax example: function(v, record)
	 *            { return ... ; }
	 */
	public void setConvert(String convert) {
		this.convert = convert;
	}



    public boolean isPkFlag() {
        return pkFlag;
    }

    public void setPkFlag(boolean pkFlag) {
        this.pkFlag = pkFlag;
    }

    /**
     * 中文字段名
     */
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public InputType getInputType() {
        return inputType;
    }

    public void setInputType(InputType inputType) {
        this.inputType = inputType;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    private final static class ModelTypeSerializer extends JsonSerializer<ModelType> {
		@Override
		public void serialize(ModelType value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
				JsonProcessingException {
			jgen.writeString(value.getJsName());

		}
	}

}
