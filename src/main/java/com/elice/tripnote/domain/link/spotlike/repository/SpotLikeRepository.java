package com.elice.tripnote.domain.link.spotlike.repository;

import com.elice.tripnote.domain.link.spotlike.entity.SpotLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpotLikeRepository extends JpaRepository<SpotLike, Long> {

    SpotLike findBySpotIdAndMemberId(Long spotId, Long memberId);

}
