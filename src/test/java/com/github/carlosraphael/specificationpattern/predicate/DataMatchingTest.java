package com.github.carlosraphael.specificationpattern.predicate;

import com.github.carlosraphael.specificationpattern.FxTransaction;
import com.github.carlosraphael.specificationpattern.entity.Specification;
import com.github.carlosraphael.specificationpattern.entity.SpecificationOperator;
import com.github.carlosraphael.specificationpattern.entity.fieldspecification.FieldOperator;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.carlosraphael.specificationpattern.SampleDataFactory.amountOfFiveHundred;
import static com.github.carlosraphael.specificationpattern.SampleDataFactory.amountOfFiveHundredMinAndOneThousandMax;
import static com.github.carlosraphael.specificationpattern.SampleDataFactory.createFieldSpecification;
import static com.github.carlosraphael.specificationpattern.SampleDataFactory.eurCurrency;
import static com.github.carlosraphael.specificationpattern.SampleDataFactory.sourceAmountFieldMapping;
import static com.github.carlosraphael.specificationpattern.SampleDataFactory.sourceCurrencyFieldMapping;
import static com.github.carlosraphael.specificationpattern.SampleDataFactory.usdCurrency;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

/**
 * @author carlos.raphael.lopes@gmail.com
 */
public class DataMatchingTest {

    Currency EUR = Currency.getInstance("EUR");
    Currency USD = Currency.getInstance("USD");
    BigDecimal twoHundred = BigDecimal.valueOf(200);
    BigDecimal sixHundred = BigDecimal.valueOf(600);
    BigDecimal twoThousand = BigDecimal.valueOf(2000);

    @Test // criteria: sourceCurrency = "EUR"
    public void matchFxTransactionWithSourceCurrencyEqualToEUR() {
        // given
        FxTransaction eurTransaction = FxTransaction.builder().sourceCurrency(EUR).build();
        FxTransaction usdTransaction = FxTransaction.builder().sourceCurrency(USD).build();
        Stream<FxTransaction> streamOfFxTransactions = Stream.of(eurTransaction, usdTransaction);
        Specification eurSourceCurrencySpecification = createFieldSpecification(
                sourceCurrencyFieldMapping(), FieldOperator.EQUALS, eurCurrency(), SpecificationOperator.AND);

        // when
        Predicate<FxTransaction> basedOnEURsourceCurrency = Predicates.asPredicate(ImmutableSet.of(eurSourceCurrencySpecification));
        List<FxTransaction> selectedFxTransaction = streamOfFxTransactions.filter(basedOnEURsourceCurrency).collect(Collectors.toList());

        // then
        assertThat(selectedFxTransaction, hasSize(1));
        assertThat(selectedFxTransaction.get(0).getSourceCurrency(), is(equalTo(EUR)));
    }

    @Test // criteria: sourceCurrency = "EUR" OR sourceAmount > 500
    public void matchFxTransactionWithSourceCurrencyEqualToEURorSourceAmountGreaterThan500() {
        // given
        FxTransaction eurTransactionLessThan500 = FxTransaction.builder().sourceCurrency(EUR).sourceAmount(twoHundred).build();
        FxTransaction usdTransactionLessThan500 = FxTransaction.builder().sourceCurrency(USD).sourceAmount(twoHundred).build();
        FxTransaction usdTransactionGreaterThan500 = FxTransaction.builder().sourceCurrency(USD).sourceAmount(sixHundred).build();
        Stream<FxTransaction> streamOfFxTransactions = Stream.of(
                eurTransactionLessThan500, usdTransactionLessThan500, usdTransactionGreaterThan500);
        Specification eurSourceCurrencySpecification = createFieldSpecification(
                sourceCurrencyFieldMapping(), FieldOperator.EQUALS, eurCurrency(), SpecificationOperator.OR);
        Specification sourceAmountSpecification = createFieldSpecification(
                sourceAmountFieldMapping(), FieldOperator.GREATER_THAN, amountOfFiveHundred(), SpecificationOperator.OR);

        // when
        Predicate<FxTransaction> basedOnCreatedCriteria =
                Predicates.asPredicate(ImmutableSet.of(eurSourceCurrencySpecification, sourceAmountSpecification));
        List<FxTransaction> selectedFxTransaction = streamOfFxTransactions.filter(basedOnCreatedCriteria).collect(Collectors.toList());

        // then
        assertThat(selectedFxTransaction, hasSize(2));
        assertThat(selectedFxTransaction.get(0).getSourceCurrency(), is(equalTo(EUR)));
        assertThat(selectedFxTransaction.get(0).getSourceAmount(), is(equalTo(twoHundred)));
        assertThat(selectedFxTransaction.get(1).getSourceCurrency(), is(equalTo(USD)));
        assertThat(selectedFxTransaction.get(1).getSourceAmount(), is(equalTo(sixHundred)));
    }

    @Test // criteria: sourceCurrency = "EUR" AND sourceAmount BETWEEN 500 AND 1000
    public void matchFxTransactionWithSourceCurrencyEqualToEURandSourceAmountBetween500And1000() {
        // given
        FxTransaction eurTransactionWithAmountOf200 = FxTransaction.builder().sourceCurrency(EUR).sourceAmount(twoHundred).build();
        FxTransaction eurTransactionWithAmountOf600 = FxTransaction.builder().sourceCurrency(EUR).sourceAmount(sixHundred).build();
        FxTransaction eurTransactionWithAmountOf2000 = FxTransaction.builder().sourceCurrency(EUR).sourceAmount(twoThousand).build();
        FxTransaction usdTransactionWithAmountOf600 = FxTransaction.builder().sourceCurrency(USD).sourceAmount(sixHundred).build();
        Stream<FxTransaction> streamOfFxTransactions = Stream.of(
                eurTransactionWithAmountOf200, eurTransactionWithAmountOf600, eurTransactionWithAmountOf2000, usdTransactionWithAmountOf600);
        Specification eurSourceCurrencySpecification = createFieldSpecification(
                sourceCurrencyFieldMapping(), FieldOperator.EQUALS, eurCurrency(), SpecificationOperator.AND);
        Specification sourceAmountSpecification = createFieldSpecification(
                sourceAmountFieldMapping(), FieldOperator.BETWEEN, amountOfFiveHundredMinAndOneThousandMax(), SpecificationOperator.AND);

        // when
        Predicate<FxTransaction> basedOnCreatedCriteria =
                Predicates.asPredicate(ImmutableSet.of(eurSourceCurrencySpecification, sourceAmountSpecification));
        List<FxTransaction> selectedFxTransaction = streamOfFxTransactions.filter(basedOnCreatedCriteria).collect(Collectors.toList());

        // then
        assertThat(selectedFxTransaction, hasSize(1));
        assertThat(selectedFxTransaction.get(0).getSourceCurrency(), is(equalTo(EUR)));
        assertThat(selectedFxTransaction.get(0).getSourceAmount(), is(equalTo(sixHundred)));
    }

    @Test // criteria: (sourceCurrency = "EUR" AND sourceAmount BETWEEN 500 AND 1000) OR sourceCurrency = "USD"
    public void matchFxTransactionWithSourceCurrencyEqualToEURAndSourceAmountBetween500And1000OrEvenOnlyCurrencyEqualToUSD() {
        // given
        FxTransaction eurTransactionWithAmountOf200 = FxTransaction.builder().sourceCurrency(EUR).sourceAmount(twoHundred).build();
        FxTransaction eurTransactionWithAmountOf600 = FxTransaction.builder().sourceCurrency(EUR).sourceAmount(sixHundred).build();
        FxTransaction eurTransactionWithAmountOf2000 = FxTransaction.builder().sourceCurrency(EUR).sourceAmount(twoThousand).build();
        FxTransaction usdTransactionWithAmountOf200 = FxTransaction.builder().sourceCurrency(USD).sourceAmount(twoHundred).build();
        Stream<FxTransaction> streamOfFxTransactions = Stream.of(
                eurTransactionWithAmountOf200, eurTransactionWithAmountOf600, eurTransactionWithAmountOf2000, usdTransactionWithAmountOf200);
        Specification eurSourceCurrencySpecification = createFieldSpecification(
                sourceCurrencyFieldMapping(), FieldOperator.EQUALS, eurCurrency(), SpecificationOperator.AND);
        eurSourceCurrencySpecification.addChild(createFieldSpecification(
                sourceAmountFieldMapping(), FieldOperator.BETWEEN, amountOfFiveHundredMinAndOneThousandMax(), SpecificationOperator.AND));
        Specification usdSourceCurrencySpecification = createFieldSpecification(
                sourceCurrencyFieldMapping(), FieldOperator.EQUALS, usdCurrency(), SpecificationOperator.OR);

        // when
        Predicate<FxTransaction> basedOnCreatedCriteria =
                Predicates.asPredicate(ImmutableSet.of(eurSourceCurrencySpecification, usdSourceCurrencySpecification));
        List<FxTransaction> selectedFxTransaction = streamOfFxTransactions.filter(basedOnCreatedCriteria).collect(Collectors.toList());

        // then
        assertThat(selectedFxTransaction, hasSize(2));
        assertThat(selectedFxTransaction.get(0).getSourceCurrency(), is(equalTo(EUR)));
        assertThat(selectedFxTransaction.get(0).getSourceAmount(), is(equalTo(sixHundred)));
        assertThat(selectedFxTransaction.get(1).getSourceCurrency(), is(equalTo(USD)));
        assertThat(selectedFxTransaction.get(1).getSourceAmount(), is(equalTo(twoHundred)));
    }
}
