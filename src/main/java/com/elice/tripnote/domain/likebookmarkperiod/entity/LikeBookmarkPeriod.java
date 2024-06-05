package com.elice.tripnote.domain.likebookmarkperiod.entity;

import com.elice.tripnote.domain.integratedroute.entity.IntegratedRoute;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "like_bookmark_period")
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

    @Column(name = "likes", nullable = false)
    Integer likes;

    @Column(name = "bookmark", nullable = false)
    Integer bookmark;

    @Column(name = "start_at", nullable = false)
    LocalDateTime startAt;

    @Column(name = "end_at", nullable = true)
    LocalDateTime endAt;

    @Builder
    public LikeBookmarkPeriod(IntegratedRoute integratedRoute, Integer likes, Integer bookmark) {
        this.integratedRoute = integratedRoute;
        this.likes = likes;
        this.bookmark = bookmark;
        this.startAt = LocalDateTime.now();
        this.endAt = null;
    }

    public void updateLike(int newLike) {
        this.likes = newLike;
    }

    public void updateBookmark(int newBookmark) {
        this.bookmark = newBookmark;
    }
}
