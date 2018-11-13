package com.github.carlosraphael.specificationpattern.entity;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author carlos.raphael.lopes@gmail.com
 */
@Entity @Getter @Setter
@EqualsAndHashCode(of = "customerId")
public class Subscription extends BaseEntity {

    @Column(unique = true)
    private Long customerId;
    @OrderBy("rank")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter(AccessLevel.NONE) @Getter(AccessLevel.NONE)
    private Set<Specification> specifications = new LinkedHashSet<>();

    public void addSpecification(Specification... specifications) {
        Objects.requireNonNull(specifications);
        this.specifications.addAll(Arrays.asList(specifications));
    }

    public void removeSpecification(Specification... specifications) {
        Objects.requireNonNull(specifications);
        this.specifications.removeAll(Arrays.asList(specifications));
    }

    public void changeSpecification(Specification... specifications) {
        removeSpecification(specifications);
        addSpecification(specifications);
    }

    public Set<Specification> getSpecifications() {
        return Collections.unmodifiableSet(specifications);
    }
}
