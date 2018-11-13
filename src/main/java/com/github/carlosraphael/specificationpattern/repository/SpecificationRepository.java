package com.github.carlosraphael.specificationpattern.repository;

import com.github.carlosraphael.specificationpattern.entity.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author carlos.raphael.lopes@gmail.com
 */
public interface SpecificationRepository extends JpaRepository<Specification, Long> {
}
