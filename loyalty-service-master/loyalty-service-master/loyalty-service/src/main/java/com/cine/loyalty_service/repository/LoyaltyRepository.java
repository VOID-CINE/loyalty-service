package com.cine.loyalty_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cine.loyalty_service.model.Loyalty;

public interface LoyaltyRepository extends JpaRepository<Loyalty, Long> {

    Optional<Loyalty> findByUserId(Long userId);

}
