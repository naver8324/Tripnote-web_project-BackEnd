package com.elice.tripnote.domain.spot.repository;

import com.elice.tripnote.domain.route.entity.SpotResponseDTO;

import java.util.List;

public interface CustomSpotRepository {
    List<SpotResponseDTO> findByRouteIds(Long integratedRouteId);
}
