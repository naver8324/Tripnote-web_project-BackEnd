package com.elice.tripnote.domain.likebookmarkperiod.repository;


import com.elice.tripnote.domain.integratedroute.entity.IntegratedRoute;
import com.elice.tripnote.domain.likebookmarkperiod.entity.LikeBookmarkPeriod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeBookPeriodRepository extends JpaRepository<LikeBookmarkPeriod, Long> {
    boolean existsByIntegratedRoute(IntegratedRoute integratedRoute);

    Optional<LikeBookmarkPeriod> findByIntegratedRouteId(Long integratedRouteId);

}
