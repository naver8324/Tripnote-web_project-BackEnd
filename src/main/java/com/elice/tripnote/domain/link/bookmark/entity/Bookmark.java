package com.elice.tripnote.domain.link.bookmark.entity;


import com.elice.tripnote.domain.member.entity.Member;
import com.elice.tripnote.domain.post.entity.Post;
import com.elice.tripnote.domain.route.entity.Route;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
// PUBLIC, PROTECTED가 아니면 JPA를 사용시 에러 발생함.
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bookmark{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;





    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // route와 post는 둘 중 하나는 null값이 가능하므로 주의를 기울여야 합니다.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id")
    private Route route;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    private Bookmark(Post post){
        this.post = post;
    }



}
