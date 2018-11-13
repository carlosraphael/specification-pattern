package com.github.carlosraphael.specificationpattern;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;

/**
 * @author carlos.raphael.lopes@gmail.com
 */
@Value @Builder
public class FxTransaction {

    private final Currency sourceCurrency;
    private final Currency targetCurrency;
    private final BigDecimal sourceAmount;
    private final BigDecimal targetAmount;
    private final Date created;
}
