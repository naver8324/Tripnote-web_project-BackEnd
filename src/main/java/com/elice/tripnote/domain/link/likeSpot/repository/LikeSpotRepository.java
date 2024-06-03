package com.elice.tripnote.domain.link.likeSpot.repository;

import com.elice.tripnote.domain.link.likeSpot.entity.LikeSpot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeSpotRepository extends JpaRepository<LikeSpot, Long> {
    LikeSpot findBySpotIdAndMemberId(Long spotId, Long memberId);
}
