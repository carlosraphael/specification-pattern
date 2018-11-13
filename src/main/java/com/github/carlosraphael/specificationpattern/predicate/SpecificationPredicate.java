package com.github.carlosraphael.specificationpattern.predicate;

import com.github.carlosraphael.specificationpattern.FxTransaction;
import com.github.carlosraphael.specificationpattern.entity.Specification;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author carlos.raphael.lopes@gmail.com
 */
@ToString
@EqualsAndHashCode
@AllArgsConstructor
abstract class SpecificationPredicate<T extends Specification> implements Predicate<FxTransaction> {

    final T specification;
    final Optional<Predicate<FxTransaction>> combinedPredicate;

    SpecificationPredicate<Specification> combineWith(SpecificationPredicate<Specification> other) {
        Predicate<FxTransaction> newCombinedPredicate;
        switch (other.specification.getOperator()) {
            case AND:
                newCombinedPredicate = and(other);
                break;
            case OR:
                newCombinedPredicate = or(other);
                break;
            default: throw new IllegalStateException("SpecificationOperator[" + specification.getOperator() + "] not supported");
        }
        return new CombinedSubscriptionPredicate(specification, newCombinedPredicate);
    }

    static final class CombinedSubscriptionPredicate extends SpecificationPredicate<Specification> {

        private CombinedSubscriptionPredicate(Specification specification, Predicate<FxTransaction> predicate) {
            super(specification, Optional.of(predicate));
        }

        @Override
        public boolean test(FxTransaction fxTransaction) {
            return combinedPredicate.orElseThrow(IllegalStateException::new).test(fxTransaction);
        }
    }
}
