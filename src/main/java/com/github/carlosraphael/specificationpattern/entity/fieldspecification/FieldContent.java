package com.github.carlosraphael.specificationpattern.entity.fieldspecification;

import com.github.carlosraphael.specificationpattern.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.NotNull;

/**
 * @author carlos.raphael.lopes@gmail.com
 * @param <T>
 */
@Entity @Getter @Setter
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class FieldContent<T> extends BaseEntity {

    @NotNull
    protected T value;
    protected T anotherValue;

    @Override
    public String toString() {
        return anotherValue != null ? value + " AND " + anotherValue : value.toString();
    }
}
