package com.github.carlosraphael.specificationpattern;

import com.github.carlosraphael.specificationpattern.entity.Specification;
import com.github.carlosraphael.specificationpattern.entity.SpecificationOperator;
import com.github.carlosraphael.specificationpattern.entity.SpecificationType;
import com.github.carlosraphael.specificationpattern.entity.fieldspecification.CurrencyFieldContent;
import com.github.carlosraphael.specificationpattern.entity.fieldspecification.DecimalFieldContent;
import com.github.carlosraphael.specificationpattern.entity.fieldspecification.FieldContent;
import com.github.carlosraphael.specificationpattern.entity.fieldspecification.FieldMapping;
import com.github.carlosraphael.specificationpattern.entity.fieldspecification.FieldOperator;
import com.github.carlosraphael.specificationpattern.entity.fieldspecification.FieldSpecification;
import com.github.carlosraphael.specificationpattern.entity.fieldspecification.FieldType;
import org.apache.commons.lang3.RandomUtils;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * @author carlos.raphael.lopes@gmail.com
 */
public class SampleDataFactory {

    public static FieldContent<BigDecimal> amountOfFiveHundred() {
        FieldContent<BigDecimal> fiveHundred = new DecimalFieldContent();
        fiveHundred.setValue(BigDecimal.valueOf(500));
        return fiveHundred;
    }

    public static FieldContent<BigDecimal> amountOfFiveHundredMinAndOneThousandMax() {
        FieldContent<BigDecimal> fiveHundredMinAndOneThousandMax = new DecimalFieldContent();
        fiveHundredMinAndOneThousandMax.setValue(BigDecimal.valueOf(500));
        fiveHundredMinAndOneThousandMax.setAnotherValue(BigDecimal.valueOf(1000));
        return fiveHundredMinAndOneThousandMax;
    }

    public static FieldContent<Currency> eurCurrency() {
        FieldContent<Currency> eurCurrency = new CurrencyFieldContent();
        eurCurrency.setValue(Currency.getInstance("EUR"));
        return eurCurrency;
    }

    public static FieldContent<Currency> usdCurrency() {
        FieldContent<Currency> usdCurrency = new CurrencyFieldContent();
        usdCurrency.setValue(Currency.getInstance("USD"));
        return usdCurrency;
    }

    public static Specification createFieldSpecification(FieldMapping fieldMapping, FieldOperator fieldOperator,
                                                         FieldContent expectedContent, SpecificationOperator operator) {
        FieldSpecification fieldSpecification = new FieldSpecification();
        fieldSpecification.setId(RandomUtils.nextLong());
        fieldSpecification.setType(SpecificationType.FIELD);
        fieldSpecification.setFieldOperator(fieldOperator);
        fieldSpecification.setExpectedContent(expectedContent);
        fieldSpecification.setOperator(operator);
        fieldSpecification.setFieldMapping(fieldMapping);
        return fieldSpecification;
    }


    public static FieldMapping sourceCurrencyFieldMapping() {
        return createFieldMapping("Source Currency", "sourceCurrency", FieldType.CURRENCY);
    }

    public static FieldMapping targetCurrencyFieldMapping() {
        return createFieldMapping("Target Currency", "targetCurrency", FieldType.CURRENCY);
    }

    public static FieldMapping sourceAmountFieldMapping() {
        return createFieldMapping("Source Amount", "sourceAmount", FieldType.DECIMAL);
    }

    public static FieldMapping createdFieldMapping() {
        return createFieldMapping("Created time", "created", FieldType.DATE);
    }

    public static FieldMapping createFieldMapping(String displayName, String fieldName, FieldType type) {
        FieldMapping fieldMapping = new FieldMapping();
        fieldMapping.setDisplayName(displayName);
        fieldMapping.setName(fieldName);
        fieldMapping.setType(type);
        return fieldMapping;
    }
}
