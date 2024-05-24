package com.elice.tripnote.integrated_route.repository;

import com.elice.tripnote.integrated_route.entity.IntegratedRoute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IntegratedRouteRepository extends JpaRepository<IntegratedRoute, Long> {
    Optional<IntegratedRoute> findByIntegratedRoute(String integratedRoute);
}
