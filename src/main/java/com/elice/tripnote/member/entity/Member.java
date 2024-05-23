package com.elice.tripnote.member.entity;

import com.elice.tripnote.bookmark.entity.Bookmark;
import com.elice.tripnote.comment.entity.Comment;
import com.elice.tripnote.post.entity.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false, length = 40, unique = true)
    private String email;

    @Column(length = 90)
    private String password;

    @Column(name = "oauth_id")
    private Long oauthId;

    @Column(nullable = false, length = 20, unique = true)
    private String nickname;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(nullable = false, length = 20)
    private String state;

//    해당 엔티티 pull 받으면 주석 해제 예정
    @OneToMany(mappedBy = "member")
    private List<Bookmark> bookmarks = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Post> posts = new ArrayList<>();

//    @OneToMany(mappedBy = "member")
//    private List<LikePost> likePosts = new ArrayList<>();

//    @OneToMany(mappedBy = "member")
//    private List<Route> routes = new ArrayList<>();

}
