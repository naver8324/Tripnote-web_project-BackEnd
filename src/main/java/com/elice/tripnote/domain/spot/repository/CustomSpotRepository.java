package com.elice.tripnote.domain.spot.repository;

import com.elice.tripnote.domain.route.entity.SpotResponseDTO;
import com.elice.tripnote.domain.spot.entity.Spot;

import java.util.List;

public interface CustomSpotRepository {
    List<SpotResponseDTO> findByRouteIds(Long integratedRouteId);
    List<Spot> findSpotsByRouteIdInOrder(Long routeId);
    List<Spot> findSpotsByIntegratedRouteIdInOrder(Long integratedRouteId);
}
