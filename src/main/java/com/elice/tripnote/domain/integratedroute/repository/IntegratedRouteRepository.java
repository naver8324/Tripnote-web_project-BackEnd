package com.elice.tripnote.domain.integratedroute.repository;

import com.elice.tripnote.domain.integratedroute.entity.IntegratedRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IntegratedRouteRepository extends JpaRepository<IntegratedRoute, Long> {
    Optional<IntegratedRoute> findByIntegratedRoutes(String integratedRoutes);
}
