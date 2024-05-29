package com.elice.tripnote.domain.link.reportPost.entity;

import com.elice.tripnote.domain.member.entity.Member;
import com.elice.tripnote.domain.post.entity.Post;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name="report_post")
public class ReportPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column
    private LocalDateTime reportedAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;



    public void report(){
        if(reportedAt == null){
            reportedAt = LocalDateTime.now();
            post.addReport();
            return;
        }
        reportedAt = null;
        post.removeReport();


    }





}
