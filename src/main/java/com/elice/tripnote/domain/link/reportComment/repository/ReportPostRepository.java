package com.elice.tripnote.domain.link.reportComment.repository;

import com.elice.tripnote.domain.link.reportComment.entity.ReportComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportPostRepository extends JpaRepository<ReportComment, Long> {



}
