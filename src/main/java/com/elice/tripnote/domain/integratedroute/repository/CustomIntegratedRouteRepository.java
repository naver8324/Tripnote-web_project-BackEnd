package com.elice.tripnote.domain.integratedroute.repository;

import com.elice.tripnote.domain.integratedroute.status.IntegratedRouteStatus;
import com.elice.tripnote.domain.route.entity.IntegratedRouteRegionDTO;

import java.util.List;

public interface CustomIntegratedRouteRepository {
    List<IntegratedRouteRegionDTO> findTopIntegratedRoutesByRegionAndHashtags(IntegratedRouteStatus region, List<Long> hashtags);
}
