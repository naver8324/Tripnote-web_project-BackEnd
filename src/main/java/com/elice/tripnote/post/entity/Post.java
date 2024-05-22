package com.elice.tripnote.post.entity;


import com.elice.tripnote.comment.entity.Comment;
import com.elice.tripnote.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String title;


    // 사용하는 db에 따라 @Lob를 사용할 때는 큰 주의가 필요합니다. postgre와 oracle은 특히 주의해야 합니다.
    @Lob
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int likes;


    @Column(nullable = false)
    @ColumnDefault("0")
    private int report;

    @Column
    private boolean isDeleted = false;


    // USER, ROUTE 객체가 생성 되면 주석을 풀 예정입니다.
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "route_id", nullable = false)
//    private Route route;


    // 빈 객체로 초기화하는 것이 좋습니다. NullPointerException, LazyInitializationException 방지.
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
    private List<Comment> Comments= new ArrayList<>();


}
