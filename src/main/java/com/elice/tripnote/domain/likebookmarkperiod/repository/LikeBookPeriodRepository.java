package com.elice.tripnote.domain.likebookmarkperiod.repository;


import com.elice.tripnote.domain.integratedroute.entity.IntegratedRoute;
import com.elice.tripnote.domain.likebookmarkperiod.entity.LikeBookmarkPeriod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeBookPeriodRepository extends JpaRepository<LikeBookmarkPeriod, Long> {
    boolean existsByIntegratedRoute(IntegratedRoute integratedRoute);
}
