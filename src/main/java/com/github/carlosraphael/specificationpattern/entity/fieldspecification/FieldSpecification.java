package com.github.carlosraphael.specificationpattern.entity.fieldspecification;

import com.github.carlosraphael.specificationpattern.entity.Specification;
import com.github.carlosraphael.specificationpattern.entity.SpecificationType;
import com.github.carlosraphael.specificationpattern.entity.fieldspecification.FieldType.Constants;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.MetaValue;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;

/**
 * @author carlos.raphael.lopes@gmail.com
 */
@Entity @Getter @Setter
public class FieldSpecification extends Specification {

    @ManyToOne @NotNull
    private FieldMapping fieldMapping = new FieldMapping();

    @Enumerated(EnumType.STRING) @NotNull
    private FieldOperator fieldOperator = FieldOperator.EQUALS;

    @NotNull
    @Any(metaColumn = @Column(name = "expected_content_type"), optional = false)
    @AnyMetaDef(metaType = "string", idType = "long", metaValues = {
            @MetaValue(value = Constants.TEXT_TYPE, targetEntity = TextFieldContent.class),
            @MetaValue(value = Constants.INTEGER_TYPE, targetEntity = IntegerFieldContent.class),
            @MetaValue(value = Constants.DECIMAL_TYPE, targetEntity = DecimalFieldContent.class),
            @MetaValue(value = Constants.CURRENCY_TYPE, targetEntity = CurrencyFieldContent.class),
            @MetaValue(value = Constants.DATE_TYPE, targetEntity = DateFieldContent.class)
    })
    @JoinColumn(name = "expectedContentId")
    @Cascade(CascadeType.ALL)
    private FieldContent expectedContent = new TextFieldContent();

    @Override @PrePersist
    protected void onCreate() {
        super.onCreate();
        setType(SpecificationType.FIELD);
    }

    @Override
    public String toString() {
        return fieldMapping.getName() + " [" + fieldOperator + "] " + expectedContent;
    }
}
