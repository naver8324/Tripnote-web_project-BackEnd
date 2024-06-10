package com.elice.tripnote.domain.integratedroute.entity;

import com.elice.tripnote.domain.likebookmarkperiod.entity.LikeBookmarkPeriod;
import com.elice.tripnote.domain.link.uuidhashtag.entity.UUIDHashtag;
import com.elice.tripnote.domain.route.status.RouteStatus;
import com.elice.tripnote.domain.spot.constant.Region;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "integrated_route")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class IntegratedRoute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "integrated_routes", nullable = false)
    String integratedRoutes;

    @Enumerated(EnumType.STRING)
    @Column(name = "region", nullable = true)
    Region region;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    RouteStatus routeStatus;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "integratedRoute")
    @JsonIgnore
    List<UUIDHashtag> uuidHashtags = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "integratedRoute")
    @JsonIgnore
    List<LikeBookmarkPeriod> likeBookmarkPeriods = new ArrayList<>();

    @Builder
    public IntegratedRoute(String integratedRoutes, Region region, RouteStatus routeStatus) {
        this.integratedRoutes = integratedRoutes;
        this.region = region;
        this.routeStatus = routeStatus;
    }

    @Builder
    public IntegratedRoute(String integratedRoutes, Region region, List<UUIDHashtag> uuidHashtags, List<LikeBookmarkPeriod> likeBookmarkPeriods) {
        this.integratedRoutes = integratedRoutes;
        this.region = region;
        this.uuidHashtags = uuidHashtags;
        this.likeBookmarkPeriods = likeBookmarkPeriods;
    }


}
