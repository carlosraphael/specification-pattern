package com.github.carlosraphael.specificationpattern.entity.fieldspecification;

import com.github.carlosraphael.specificationpattern.FxTransaction;
import com.github.carlosraphael.specificationpattern.entity.BaseEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Pre-loaded entity containing all possible fields from the defined domain, in this case {@link FxTransaction}, that
 * can be used to filter data out.
 *
 * e.g. sourceAmount, sourceCurrency, targetCurrency, created, etc.
 *
 * @author carlos.raphael.lopes@gmail.com
 */
@Entity @Getter @Setter
@EqualsAndHashCode(of = "name")
public class FieldMapping extends BaseEntity {

    @NotBlank
    @Column(unique = true)
    private String name;
    @NotNull
    @Enumerated(EnumType.STRING)
    private FieldType type = FieldType.TEXT;
    @NotBlank
    private String displayName;

    @Override
    public String toString() {
        return name;
    }
}
