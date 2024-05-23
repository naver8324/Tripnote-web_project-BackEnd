package com.elice.tripnote.likePost.entity;

import com.elice.tripnote.comment.entity.Comment;
import com.elice.tripnote.global.entity.BaseTimeEntity;
import com.elice.tripnote.member.entity.Member;
import com.elice.tripnote.post.entity.Post;
import com.elice.tripnote.post.entity.PostResponseDTO;
import com.elice.tripnote.route.entity.Route;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
