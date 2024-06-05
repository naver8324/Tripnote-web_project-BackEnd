package com.elice.tripnote.domain.route.repository;

import com.elice.tripnote.domain.route.entity.Route;
import com.elice.tripnote.domain.route.entity.RouteIdNameDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomRouteRepository {
    List<Long> findIntegratedRouteIdsBySpots(List<Long> spots);
    List<RouteIdNameDTO> findLikedRoutesByMemberId(Long memberId);
    Page<RouteIdNameDTO> findMarkedRoutesByMemberId(Long memberId, Pageable pageable);
    Page<RouteIdNameDTO> findRoutesByMemberId(Long memberId, Pageable pageable);
    int getIntegratedRouteLikeCounts(Long integratedRouteId);
    Route getMinRouteByIntegratedId(Long integratedId);


}
