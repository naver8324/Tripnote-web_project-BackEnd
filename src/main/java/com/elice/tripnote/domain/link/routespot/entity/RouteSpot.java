package com.elice.tripnote.domain.link.routespot.entity;

import com.elice.tripnote.domain.integratedroute.entity.IntegratedRoute;
import com.elice.tripnote.domain.member.entity.Member;
import com.elice.tripnote.domain.route.entity.Route;
import com.elice.tripnote.domain.route.status.RouteStatus;
import com.elice.tripnote.domain.spot.entity.Spot;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "route_spot")
public class RouteSpot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spot_id", nullable = false)
    private Spot spot;

    @Column(nullable = false)
    private int sequence;

    @Column(nullable = true)
    private Long nextSpotId;

    @Builder
    public RouteSpot(Route route, Spot spot, int sequence, Long nextSpotId){
        this.route=route;
        this.spot=spot;
        this.sequence=sequence;
        this.nextSpotId = nextSpotId;
    }
}
