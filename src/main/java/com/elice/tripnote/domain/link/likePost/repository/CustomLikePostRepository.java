package com.elice.tripnote.domain.link.likePost.repository;

public interface CustomLikePostRepository {
    boolean existsByMemberIdAndIntegratedRouteId(Long memberId, Long integratedId);

    void deleteByMemberIdAndIntegratedRouteId(Long memberId, Long integratedId);
}
