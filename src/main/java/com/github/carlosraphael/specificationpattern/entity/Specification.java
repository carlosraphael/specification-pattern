package com.github.carlosraphael.specificationpattern.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author carlos.raphael.lopes@gmail.com
 */
@Entity @Getter @Setter
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Specification extends BaseEntity {

    @NotNull
    private Integer rank;

    @Column(name = "specificationType")
    @Enumerated(EnumType.STRING) @NotNull
    private SpecificationType type = SpecificationType.FIELD;

    @Enumerated(EnumType.STRING) @NotNull
    private SpecificationOperator operator = SpecificationOperator.AND;

    @ManyToOne(fetch = FetchType.LAZY)
    private Specification parent;

    @OrderBy("rank")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "parent")
    @LazyCollection(LazyCollectionOption.FALSE)
    @Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE)
    private Set<Specification> children = new LinkedHashSet<>();

    public void addChild(Specification child) {
        child.setParent(this);
        children.add(child);
    }

    public void addChildren(Specification... childrenSpec) {
        Objects.requireNonNull(childrenSpec);
        Stream.of(childrenSpec).forEach(this::addChild);
    }

    public void changeChildren(Specification... childrenSpec) {
        removeChildren(childrenSpec);
        addChildren(childrenSpec);
    }

    public void removeChildren(Specification... childrenSpec) {
        Objects.requireNonNull(childrenSpec);
        children.removeAll(Arrays.asList(childrenSpec));
    }

    public Stream<Specification> flattened() {
        return Stream.concat(
                Stream.of(this),
                children.stream().flatMap(Specification::flattened));
    }

    public Set<Specification> getChildren() {
        return Collections.unmodifiableSet(children);
    }
}
