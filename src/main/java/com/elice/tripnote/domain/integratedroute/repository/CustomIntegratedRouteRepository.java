package com.elice.tripnote.domain.integratedroute.repository;

import com.elice.tripnote.domain.spot.constant.Region;

import java.util.List;

public interface CustomIntegratedRouteRepository {
    List<Long> findTopIntegratedRoutesByRegionAndHashtags(Region region);
    void deleteIntegratedRoute(Long integratedRouteId);
}
