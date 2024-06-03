package com.elice.tripnote.domain.integratedroute.repository;

import com.elice.tripnote.domain.route.entity.IntegratedRouteDTO;
import com.elice.tripnote.domain.spot.constant.Region;

import java.util.List;

public interface CustomIntegratedRouteRepository {
    List<IntegratedRouteDTO> findTopIntegratedRoutesByRegionAndHashtags(Region region, List<Long> hashtags);
    List<IntegratedRouteDTO> findIntegratedRouteFilterByHashtags(List<Long> integratedIds, List<Long> hashtags);
}
