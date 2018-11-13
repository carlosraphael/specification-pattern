package com.github.carlosraphael.specificationpattern.repository;

import com.github.carlosraphael.specificationpattern.entity.fieldspecification.FieldMapping;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author carlos.raphael.lopes@gmail.com
 */
public interface FieldMappingRepository extends JpaRepository<FieldMapping, Long> {
}
