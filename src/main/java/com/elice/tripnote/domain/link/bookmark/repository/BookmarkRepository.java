package com.elice.tripnote.domain.link.bookmark.repository;


import com.elice.tripnote.domain.link.bookmark.entity.Bookmark;
import com.elice.tripnote.domain.link.reportPost.entity.ReportPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface BookmarkRepository extends JpaRepository<Bookmark, Long>, CustomBookmarkRepository {
    Bookmark findByPostIdAndMemberId(Long postId, Long memberId);

    Bookmark findByRouteIdAndMemberId(Long routeId, Long memberId);
}
