package com.elice.tripnote.domain.link.reportComment.repository;

import com.elice.tripnote.domain.link.reportComment.entity.ReportComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportCommentRepository extends JpaRepository<ReportComment, Long> {


    ReportComment findByCommentIdAndMemberId(Long commentId, Long memberId);
}
