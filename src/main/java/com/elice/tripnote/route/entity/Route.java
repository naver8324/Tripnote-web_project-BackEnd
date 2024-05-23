package com.elice.tripnote.route.entity;

import com.elice.tripnote.route.status.RouteStatus;
import com.elice.tripnote.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "route")
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uuid_id", nullable = false)
    private IntergratedRoute intergratedRoute;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RouteStatus routeStatus;

    @Column(nullable = true)
    private int expense;
}
