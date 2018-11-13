package com.github.carlosraphael.specificationpattern.predicate;

import com.github.carlosraphael.specificationpattern.entity.Specification;
import com.github.carlosraphael.specificationpattern.entity.SpecificationOperator;
import com.github.carlosraphael.specificationpattern.entity.fieldspecification.FieldOperator;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;

import java.util.Collections;
import java.util.Set;
import java.util.function.Predicate;

import static com.github.carlosraphael.specificationpattern.SampleDataFactory.amountOfFiveHundred;
import static com.github.carlosraphael.specificationpattern.SampleDataFactory.createFieldSpecification;
import static com.github.carlosraphael.specificationpattern.SampleDataFactory.eurCurrency;
import static com.github.carlosraphael.specificationpattern.SampleDataFactory.sourceAmountFieldMapping;
import static com.github.carlosraphael.specificationpattern.SampleDataFactory.sourceCurrencyFieldMapping;
import static java.util.Optional.empty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

/**
 * @author carlos.raphael.lopes@gmail.com
 */
public class PredicatesTest {

    @Test
    public void predicateIsCreatedBasedOnOneSpecification() {
        // given
        Specification specification = createFieldSpecification(
                sourceCurrencyFieldMapping(), FieldOperator.EQUALS, eurCurrency(), SpecificationOperator.AND);

        // when
        Predicate javaPredicate = Predicates.asPredicate(ImmutableSet.of(specification));

        // then
        assertThat(javaPredicate, is(notNullValue()));
        assertThat(((SpecificationPredicate)javaPredicate).combinedPredicate, is(equalTo(empty())));
    }

    @Test
    public void predicateIsCreatedBasedOnCombinedOfTwoSpecification() {
        // given
        Specification specificationForSourceCurrency = createFieldSpecification(
                sourceCurrencyFieldMapping(), FieldOperator.EQUALS, eurCurrency(), SpecificationOperator.AND);
        Specification specificationForSourceAmount = createFieldSpecification(
                sourceAmountFieldMapping(), FieldOperator.EQUALS, amountOfFiveHundred(), SpecificationOperator.AND);

        // when
        Predicate javaPredicate = Predicates.asPredicate(ImmutableSet.of(
                specificationForSourceCurrency, specificationForSourceAmount));

        // then
        assertThat(javaPredicate, is(notNullValue()));
        assertThat(((SpecificationPredicate)javaPredicate).combinedPredicate, not(equalTo(empty())));
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionIsThrownWhenNoSpecificationIsSupplied() {
        // given
        Set<Specification> nullSpecification = null;

        // when
        Predicates.asPredicate(nullSpecification);

        // then NullPointerException is thrown
    }

    @Test(expected = IllegalStateException.class)
    public void illegalStateExceptionIsThrownWhenEmptySpecificationIsSupplied() {
        // given
        Set<Specification> emptySpecification = Collections.EMPTY_SET;

        // when
        Predicates.asPredicate(emptySpecification);

        // then NullPointerException is thrown
    }
}
