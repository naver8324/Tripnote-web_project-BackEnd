package com.elice.tripnote.integrated_route.entity;

import com.elice.tripnote.like_bookmark_period.entity.LikeBookmarkPeriod;
import com.elice.tripnote.uuid_hashtag.entity.UUID_Hashtag;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="integrated_route")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class IntegratedRoute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "integrated_route", nullable = false)
    String integrated_route;

    @Column(name = "region", nullable = true)
    String region;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "integratedRoute")
    @JsonIgnore
    List<UUID_Hashtag> uuid_hashtags = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "integratedRoute")
    @JsonIgnore
    List<LikeBookmarkPeriod> like_bookmark_periods = new ArrayList<>();
    @Builder
    public IntegratedRoute(String integrated_route, String region, List<UUID_Hashtag> uuid_hashtags, List<LikeBookmarkPeriod>like_bookmark_periods){
        this.integrated_route=integrated_route;
        this.region=region;
        this.uuid_hashtags=uuid_hashtags;
        this.like_bookmark_periods=like_bookmark_periods;
    }
}
