package com.github.carlosraphael.specificationpattern.predicate;

import com.github.carlosraphael.specificationpattern.entity.fieldspecification.FieldSpecification;
import com.github.carlosraphael.specificationpattern.entity.Specification;
import com.github.carlosraphael.specificationpattern.entity.SpecificationType;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author carlos.raphael.lopes@gmail.com
 */
public final class Predicates {

    private Predicates() {}

    public static Predicate<FxTransaction> asPredicate(Set<Specification> specifications) {
        Objects.requireNonNull(specifications);
        return specifications.stream()
                .map(Specification::flattened)
                .map(Predicates::toSpecificationPredicate)
                .reduce(SpecificationPredicate::combineWith)
                .orElseThrow(IllegalStateException::new);
    }

    private static SpecificationPredicate toSpecificationPredicate(Stream<Specification> specificationStream) {
        return specificationStream
                .map(Predicates::toSpecificationPredicate)
                .reduce(SpecificationPredicate::combineWith)
                .orElseThrow(IllegalStateException::new);
    }

    private static SpecificationPredicate toSpecificationPredicate(Specification specification) {
        if (specification.getType().equals(SpecificationType.FIELD)) {
            return new FieldSpecificationPredicate((FieldSpecification) specification, Optional.empty());
        }
        throw new IllegalStateException("SpecificationType[" + specification.getType() + "] not supported");
    }
}
