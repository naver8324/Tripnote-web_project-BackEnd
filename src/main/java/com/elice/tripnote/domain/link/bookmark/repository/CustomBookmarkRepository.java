package com.elice.tripnote.domain.link.bookmark.repository;

public interface CustomBookmarkRepository {
    int getBookmarkCount(Long integratedRouteId);
    boolean existsByMemberIdAndIntegratedRouteId(Long memberId, Long integratedId);

    void deleteByMemberIdAndIntegratedRouteId(Long memberId, Long integratedId);
}
