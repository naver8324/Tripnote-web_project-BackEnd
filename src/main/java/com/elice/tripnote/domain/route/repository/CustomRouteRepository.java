package com.elice.tripnote.domain.route.repository;

import com.elice.tripnote.domain.route.entity.Route;
import com.elice.tripnote.domain.route.entity.RouteIdNameDTO;

import java.util.List;

public interface CustomRouteRepository {
    List<Long> findIntegratedRouteIdsBySpots(List<Long> spots);
    List<RouteIdNameDTO> findLikedRoutesByMemberId(Long memberId);
    List<RouteIdNameDTO> findMarkedRoutesByMemberId(Long memberId);
    List<RouteIdNameDTO> findRoutesByMemberId(Long memberId);
    int getIntegratedRouteLikeCounts(Long integratedRouteId);
    Route getMinRouteByIntegratedId(Long integratedId);


}
