package com.elice.tripnote.domain.route.repository;

import com.elice.tripnote.domain.route.entity.RecommendedRouteResponseDTO;
import com.elice.tripnote.domain.route.entity.Route;
import com.elice.tripnote.domain.route.entity.RouteDetailResponseDTO;
import com.elice.tripnote.domain.route.entity.RouteIdNameResponseDTO;
import com.elice.tripnote.global.entity.PageRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface CustomRouteRepository {
    int getIntegratedRouteLikeCounts(Long integratedRouteId);
//    Map<Long, Integer> getIntegratedRouteLikeCounts(List<Long> integratedIds);
    Route getMinRouteByIntegratedId(Long integratedId);
//    Long findPostIdByIntegratedRouteId(Long integratedId);
//    Map<Long, Long> findPostIdsByIntegratedRouteIds(List<Long> integratedIds);

    boolean findHashtagIdIdCity(Long hashtagId);
    //    Page<RouteDetailResponseDTO> findRouteDetailsByMemberId(Long memberId, Pageable pageable);
    Page<RouteDetailResponseDTO> findRouteDetailsByMemberId(Long memberId, PageRequestDTO pageRequestDTO, boolean isBookmark);
    List<RecommendedRouteResponseDTO> getRecommendedRoutes(List<Long> integratedRouteIds, Long memberId, boolean isMember);
    List<Long> findIntegratedRouteIdsBySpotsAndLikes(List<Long> spots);
    boolean existsByMemberIdAndIntegratedRouteId(Long memberId, Long integratedId, boolean isLike);
    void deleteByMemberIdAndIntegratedRouteId(Long memberId, Long integratedId, boolean isLike);


}
