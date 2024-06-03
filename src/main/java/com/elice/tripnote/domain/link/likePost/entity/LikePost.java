package com.elice.tripnote.domain.link.likePost.entity;

import com.elice.tripnote.domain.member.entity.Member;
import com.elice.tripnote.domain.post.entity.Post;
import com.elice.tripnote.domain.route.entity.Route;
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
    private LocalDateTime likedAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = true)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = true)
    private Route route;



    public void like(){
        if(likedAt == null){
            likedAt = LocalDateTime.now();
            post.addLikes();
            return;
        }
        likedAt = null;
        post.removeLikes();


    }





}
