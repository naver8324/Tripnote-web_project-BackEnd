package com.elice.tripnote.domain.route.repository;

import com.elice.tripnote.domain.link.likePost.entity.LikePost;
import com.elice.tripnote.domain.route.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface RouteRepository extends JpaRepository<Route, Long>, CustomRouteRepository {
    Route findByIdAndMemberId(Long routeId, Long memberId);
}
