package com.elice.tripnote.domain.integratedroute.repository;

import com.elice.tripnote.domain.integratedroute.status.IntegratedRouteStatus;
import com.elice.tripnote.domain.route.entity.IntegratedRouteDTO;

import java.util.List;

public interface CustomIntegratedRouteRepository {
    List<IntegratedRouteDTO> findTopIntegratedRoutesByRegionAndHashtags(IntegratedRouteStatus region, List<Long> hashtags);
    List<IntegratedRouteDTO> findIntegratedRouteFilterByHashtags(List<Long> integratedIds, List<Long> hashtags);
}
