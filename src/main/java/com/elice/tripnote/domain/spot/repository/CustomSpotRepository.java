package com.elice.tripnote.domain.spot.repository;

import com.elice.tripnote.domain.route.entity.SpotResponseDTO;
import com.elice.tripnote.domain.spot.entity.Spot;
import com.elice.tripnote.domain.spot.entity.SpotRegionDTO;

import java.util.List;
import java.util.Map;

public interface CustomSpotRepository {
    List<SpotResponseDTO> findByRouteIds(Long integratedRouteId);
//    List<Spot> findSpotsByRouteIdInOrder(Long routeId);
    List<Spot> findSpotsByIntegratedRouteIdInOrder(Long integratedRouteId);
    SpotRegionDTO getRegionByspotId(Long spotId);
    Map<Long, List<Spot>> findSpotsByIntegratedRouteIds(List<Long> integratedIds);
}
