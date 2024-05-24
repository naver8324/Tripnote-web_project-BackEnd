package com.elice.tripnote.domain.link.likePost.entity;

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
@Table(name="like_post")
public class LikePost{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;



    public void like(){
        if(createdAt == null){
            createdAt = LocalDateTime.now();
            return;
        }
        createdAt = null;

    }



}
