package com.elice.tripnote.bookmark.entity;


import com.elice.tripnote.post.entity.Post;
import jakarta.persistence.*;

@Entity
public class Bookmark{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;





    // USER, ROUTE 객체가 생성 되면 주석을 풀 예정입니다.
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;

    // route와 post는 둘 중 하나는 null값이 가능하므로 주의를 기울여야 합니다.

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "route_id")
//    private Route route;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;



}
