package com.elice.tripnote.like_bookmark_period.entity;

import com.elice.tripnote.integrated_route.entity.IntegratedRoute;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name="like_bookmark_period")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LikeBookmarkPeriod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

//    @Column(name = "uuid_id", nullable = false)
//    Long uuid_id;
    @ManyToOne
    @JoinColumn(name = "integerated_route_id", nullable = false)
    IntegratedRoute integratedRoute;

    @Column(name = "like", nullable = false)
    Integer like;

    @Column(name = "bookmark", nullable = false)
    Integer bookmark;

    @Column(name = "start_at", nullable = false)
    LocalDateTime start_at;

    @Column(name = "end_at", nullable = true)
    LocalDateTime end_at;

    @Builder
    public LikeBookmarkPeriod(IntegratedRoute integratedRoute, Integer like, Integer bookmark){
        this.integratedRoute=integratedRoute;
        this.like=like;
        this.bookmark=bookmark;
        this.start_at = LocalDateTime.now();
        this.end_at=null;
    }
}
