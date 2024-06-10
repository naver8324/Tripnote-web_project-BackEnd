package com.elice.tripnote.domain.integratedroute.repository;

import com.elice.tripnote.domain.spot.constant.Region;

import java.util.List;

public interface CustomIntegratedRouteRepository {
    List<Long> findTopIntegratedRoutesByRegionAndHashtags(Region region);
    List<Long> findIntegratedRoute(List<Long> integratedIds);
    //List<Long> findIntegratedRouteFilterByHashtags(List<Long> integratedIds, List<Long> hashtags);
    void deleteIntegratedRoute(Long integratedRouteId);
}
