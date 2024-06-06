package com.elice.tripnote.domain.link.routespot.repository;


import com.elice.tripnote.domain.link.routespot.entity.RouteSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface RouteSpotRepository extends JpaRepository<RouteSpot, Long> {
    @Query("SELECT rs FROM RouteSpot rs WHERE rs.spot.id = :spotId AND rs.nextSpotId IS NOT NULL")
    List<RouteSpot> findBySpotId(@Param("spotId") Long spotId);
}
