package com.github.carlosraphael.specificationpattern.entity.fieldspecification;

import lombok.AllArgsConstructor;
import lombok.ToString;

/**
 * @author carlos.raphael.lopes@gmail.com
 */
@AllArgsConstructor
@ToString(of = "value")
public enum FieldType {

    INTEGER(Constants.INTEGER_TYPE),
    DECIMAL(Constants.DECIMAL_TYPE),
    TEXT(Constants.TEXT_TYPE),
    DATE(Constants.DATE_TYPE),
    CURRENCY(Constants.CURRENCY_TYPE);

    private final String value;

    public static class Constants {
        public static final String INTEGER_TYPE = "INTEGER";
        public static final String DECIMAL_TYPE = "DECIMAL";
        public static final String TEXT_TYPE = "TEXT";
        public static final String DATE_TYPE = "DATE";
        public static final String CURRENCY_TYPE = "CURRENCY";
    }
}
