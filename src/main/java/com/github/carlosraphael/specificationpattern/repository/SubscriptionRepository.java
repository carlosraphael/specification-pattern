package com.github.carlosraphael.specificationpattern.repository;

import com.github.carlosraphael.specificationpattern.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author carlos.raphael.lopes@gmail.com
 */
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
}
