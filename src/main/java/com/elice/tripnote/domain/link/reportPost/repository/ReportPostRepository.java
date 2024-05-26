package com.elice.tripnote.domain.link.reportPost.repository;

import com.elice.tripnote.domain.link.reportPost.entity.ReportPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportPostRepository extends JpaRepository<ReportPost, Long> {

    ReportPost findByPostIdAndMemberId(Long postId, Long memberId);


}
