package com.github.carlosraphael.specificationpattern.predicate;

import com.github.carlosraphael.specificationpattern.FxTransaction;
import com.github.carlosraphael.specificationpattern.entity.fieldspecification.FieldContent;
import com.github.carlosraphael.specificationpattern.entity.fieldspecification.FieldMapping;
import com.github.carlosraphael.specificationpattern.entity.fieldspecification.FieldOperator;
import com.github.carlosraphael.specificationpattern.entity.fieldspecification.FieldSpecification;
import com.github.carlosraphael.specificationpattern.entity.fieldspecification.FieldType;
import lombok.extern.slf4j.Slf4j;

import java.util.Currency;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import static com.github.carlosraphael.specificationpattern.util.JavaBeanUtil.getFieldValue;

/**
 * @author carlos.raphael.lopes@gmail.com
 */
@Slf4j
@SuppressWarnings("unchecked")
class FieldSpecificationPredicate extends SpecificationPredicate<FieldSpecification> {

    FieldSpecificationPredicate(FieldSpecification specification,
                                       Optional<Predicate<FxTransaction>> combinedPredicate) {
        super(specification, combinedPredicate);
    }

    @Override
    public boolean test(FxTransaction fxTransaction) {
        FieldMapping fieldMapping = specification.getFieldMapping();

        if (fieldMapping.getType().equals(FieldType.CURRENCY)) {
            FieldContent<Currency> expectedContent = specification.getExpectedContent();
            return testCurrency(
                    specification.getFieldOperator(),
                    getFieldValue(fxTransaction, fieldMapping.getName()),
                    expectedContent.getValue());
        }

        FieldContent<Comparable> expectedContent = specification.getExpectedContent();
        return test(
                specification.getFieldOperator(),
                getFieldValue(fxTransaction, fieldMapping.getName()),
                expectedContent.getValue(),
                Optional.ofNullable(expectedContent.getAnotherValue())
        );
    }

    private static boolean testCurrency(FieldOperator fieldOperator, Currency actualValue, Currency expectedValue) {
        return test(
                fieldOperator,
                Objects.nonNull(actualValue) ? actualValue.getCurrencyCode() : null,
                expectedValue.getCurrencyCode(),
                Optional.empty()
        );
    }

    private static boolean test(FieldOperator fieldOperator, Comparable actualValue, Comparable expectedValue,
                                Optional<Comparable> expectedAnotherValue) {
        boolean result;
        switch (fieldOperator) {
            case EQUALS:
                result = expectedValue.compareTo(actualValue) == 0;
                break;
            case GREATER_THAN:
                result = expectedValue.compareTo(actualValue) < 0;
                break;
            case LESS_THAN:
                result = expectedValue.compareTo(actualValue) > 0;
                break;
            case BETWEEN:
                result = expectedValue.compareTo(actualValue) <= 0 &&
                        expectedAnotherValue.orElseThrow(IllegalStateException::new).compareTo(actualValue) >= 0;
                break;
            default: throw new IllegalStateException("Field operator not supported");
        }
        log.debug("ActualValue={} [{}] ExpectedValue={} AND ExpectedAnotherValue={}, TestResult={}",
                actualValue, fieldOperator, expectedValue, expectedAnotherValue.orElse(null), result);
        return result;
    }
}
