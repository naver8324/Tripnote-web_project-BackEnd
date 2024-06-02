package com.elice.tripnote.domain.route.repository;

import java.util.List;

public interface CustomRouteRepository {
    List<Long> findIntegratedRouteIdsBySpots(List<Long> spots);
}
