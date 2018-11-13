package com.github.carlosraphael.specificationpattern.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * @author carlos.raphael.lopes@gmail.com
 */
@Data
@MappedSuperclass
public class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    protected Date createdTime;
    @Temporal(TemporalType.TIMESTAMP)
    protected Date lastUpdatedTime;

    @PrePersist
    protected void onCreate() {
        lastUpdatedTime = createdTime = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdatedTime = new Date();
    }
}
