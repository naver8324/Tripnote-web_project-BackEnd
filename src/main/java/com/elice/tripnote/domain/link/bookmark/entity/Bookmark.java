package com.elice.tripnote.domain.link.bookmark.entity;


import com.elice.tripnote.domain.member.entity.Member;
import com.elice.tripnote.domain.post.entity.Post;
import com.elice.tripnote.domain.route.entity.Route;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Bookmark{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column
    private LocalDateTime markedAt;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // route와 post는 둘 중 하나는 null값이 가능하므로 주의를 기울여야 합니다.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id")
    private Route route;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;


    public void mark(Route route, Post post){
        if(route !=null){
            if(markedAt == null){
                markedAt = LocalDateTime.now();
                return;
            }
            markedAt = null;
            return;
        }

        if(post != null){
            if(markedAt == null){
                markedAt = LocalDateTime.now();
                return;
            }
            markedAt = null;
        }



    }






}
