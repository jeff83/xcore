package com.cmbc.codegenerator.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

    /**
     * Enumeration of all types that are valid in a ExtJS and Touch model object.
     */
    public enum ModelFieldType {

        AUTO("auto") {
            @Override
            public boolean supports(String dataType) {
                return false;
            }
        },

        INTEGER("int") {
            @Override
            public boolean supports(String dataType) {
                return dataType.toUpperCase().startsWith("NUMBER") ||(dataType.toUpperCase().startsWith("DECIMAL") && dataType.toUpperCase().indexOf(",")==-1);
            }
        },
        FLOAT("float") {
            @Override
            public boolean supports(String dataType) {
                return dataType.toUpperCase().startsWith("FLOAT") ||(dataType.toUpperCase().startsWith("DECIMAL") && dataType.toUpperCase().indexOf(",")!=-1);
            }
        },
        STRING("string") {
            @Override
            public boolean supports(String dataType) {
                return dataType.toUpperCase().startsWith("VARCHAR")||dataType.toUpperCase().startsWith("CHAR")||dataType.toUpperCase().startsWith("NVARCHAR")||dataType.toUpperCase().startsWith("NCHAR");
            }
        },
        DATE("date") {
            @Override
            public boolean supports(String dataType) {
                return dataType.toUpperCase().startsWith("DATE");
            }
        };

        private String type;

        private ModelFieldType(String type) {
            this.type = type;
        }

        /**
         * @return the name of the type for JS code
         */
        public String getType() {
            return type;
        }

        /**
         * @param dataType any class
         * @return true if the type supports the provided Java class
         */
        public abstract boolean supports(String dataType);

        public static ModelFieldType getByDateType(String dataType){
            ModelFieldType modelType = null;
            for (ModelFieldType mt : ModelFieldType.values()) {
                if (mt.supports(dataType)) {
                    modelType = mt;
                    break;
                }
            }
            return modelType;
        }

    }

