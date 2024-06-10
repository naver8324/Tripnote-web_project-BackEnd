package com.elice.tripnote.domain.route.repository;

import com.elice.tripnote.domain.route.entity.Route;
import com.elice.tripnote.domain.route.entity.RouteDetailResponseDTO;
import com.elice.tripnote.domain.route.entity.RouteIdNameResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomRouteRepository {
    List<Long> findIntegratedRouteIdsBySpots(List<Long> spots);
//    List<RouteIdNameResponseDTO> findLikedRoutesByMemberId(Long memberId);
//    Page<RouteIdNameResponseDTO> findMarkedRoutesByMemberId(Long memberId, Pageable pageable);
//    Page<RouteIdNameResponseDTO> findRoutesByMemberId(Long memberId, Pageable pageable);
    int getIntegratedRouteLikeCounts(Long integratedRouteId);
    Route getMinRouteByIntegratedId(Long integratedId);
    Long findPostIdByIntegratedRouteId(Long integratedId);
    boolean findHashtagIdIdCity(Long hashtagId);
//    Page<RouteDetailResponseDTO> findRouteDetailsByMemberId(Long memberId, Pageable pageable);
    Page<RouteDetailResponseDTO> findRouteDetailsByMemberId(Long memberId, Pageable pageable, boolean isBookmark);


}
