package com.elice.tripnote.domain.integratedroute.repository;

import com.elice.tripnote.domain.integratedroute.entity.IntegratedRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface IntegratedRouteRepository extends JpaRepository<IntegratedRoute, Long>, CustomIntegratedRouteRepository{
    Optional<IntegratedRoute> findByIntegratedRoutes(String integratedRoutes);
}
