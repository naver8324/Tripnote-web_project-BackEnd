package com.elice.tripnote.domain.route.repository;

import com.elice.tripnote.domain.route.entity.Route;
import com.elice.tripnote.domain.route.entity.RouteIdNameResponseDTO;
import com.elice.tripnote.global.entity.PageRequestDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CustomRouteRepository {
    List<Long> findIntegratedRouteIdsBySpots(List<Long> spots);
    List<RouteIdNameResponseDTO> findLikedRoutesByMemberId(Long memberId);
    Page<RouteIdNameResponseDTO> findMarkedRoutesByMemberId(Long memberId, PageRequestDTO pageRequestDTO);
    Page<RouteIdNameResponseDTO> findRoutesByMemberId(Long memberId, PageRequestDTO pageRequestDTO);
    int getIntegratedRouteLikeCounts(Long integratedRouteId);
    Route getMinRouteByIntegratedId(Long integratedId);
    Long findPostIdByIntegratedRouteId(Long integratedId);
    boolean findHashtagIdIdCity(Long hashtagId);


}
