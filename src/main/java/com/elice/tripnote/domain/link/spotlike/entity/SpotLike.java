package com.elice.tripnote.domain.link.spotlike.entity;

import com.elice.tripnote.domain.member.entity.Member;
import com.elice.tripnote.domain.spot.entity.Spot;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name="spot_like")
public class SpotLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="spot_id", nullable = false)
    private Spot spot;

    @Column
    private LocalDateTime likedAt;

    public void like(){
        if(likedAt == null){
            likedAt = LocalDateTime.now();
            spot.increaseLikes();
            return;
        }
        likedAt = null;
        spot.decreaseLikes();

    }
}
