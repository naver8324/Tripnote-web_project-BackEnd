package com.elice.tripnote.domain.post.entity;


import com.elice.tripnote.domain.comment.entity.Comment;
import com.elice.tripnote.domain.link.bookmark.entity.Bookmark;
import com.elice.tripnote.domain.link.reportPost.entity.ReportPost;
import com.elice.tripnote.domain.member.entity.Member;
import com.elice.tripnote.domain.route.entity.Route;
import com.elice.tripnote.global.entity.BaseTimeEntity;
import com.elice.tripnote.domain.link.likePost.entity.LikePost;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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

    @Column(columnDefinition = "TINYINT(1)")
    @ColumnDefault("false")
    private boolean isDeleted;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;


    // 빈 객체로 초기화하는 것이 좋습니다. NullPointerException, LazyInitializationException 방지.
    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
    private List<Comment> Comments = new ArrayList<>();

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
    private List<LikePost> likePosts = new ArrayList<>();

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
    private List<ReportPost> reportPosts = new ArrayList<>();

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
    private List<Bookmark> bookmarks = new ArrayList<>();


    public void update(PostRequestDTO postDTO) {
        title = postDTO.getTitle();
        content = postDTO.getContent();
    }


    public void addLikes(){
        likes++;
    }
    public void removeLikes(){
        likes--;
    }

    public void addReport(){
        report++;
    }
    public void removeReport(){
        report--;
    }

    public void delete(){
        isDeleted = !isDeleted;
    }

}
