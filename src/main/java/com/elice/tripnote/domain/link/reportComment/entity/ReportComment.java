package com.elice.tripnote.domain.link.reportComment.entity;

import com.elice.tripnote.domain.comment.entity.Comment;
import com.elice.tripnote.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name="report_comment")
public class ReportComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column
    private LocalDateTime reportedAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;



    public void report(){
        if(reportedAt == null){
            reportedAt = LocalDateTime.now();
            comment.addReport();
            return;
        }
        reportedAt = null;
        comment.removeReport();


    }





}
