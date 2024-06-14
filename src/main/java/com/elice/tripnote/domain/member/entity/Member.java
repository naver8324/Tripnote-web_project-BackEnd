package com.elice.tripnote.domain.member.entity;

import com.elice.tripnote.domain.link.bookmark.entity.Bookmark;
import com.elice.tripnote.domain.comment.entity.Comment;
import com.elice.tripnote.domain.link.likePost.entity.LikePost;
import com.elice.tripnote.domain.post.entity.Post;
import com.elice.tripnote.domain.route.entity.Route;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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

    @Column(name = "oauth_type")
    private String oauthType;

    @Column(nullable = false, length = 20, unique = true)
    private String nickname;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private List<Bookmark> bookmarks = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private List<Comment> comments = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private List<Post> posts = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private List<LikePost> likePosts = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private List<Route> routes = new ArrayList<>();


    @PrePersist
    private void prePersist() {
        this.status = this.status == null ? Status.valueOf("ACTIVE") : this.status;
    }

    // (멤버 자신이) 회원 삭제 메서드
    public void deleteByUser() {
        this.deletedAt = LocalDateTime.now();
        this.status = Status.DELETED_BY_USER;
    }

    // (관리자가) 회원 삭제 메서드
    public void deleteByAdmin() {
        this.deletedAt = LocalDateTime.now();
        this.status = Status.DELETED_BY_ADMIN;
    }

    // (관리자가) 회원 복구 메서드
    public void restoreByAdmin() {
        this.deletedAt = null;
        this.status = Status.ACTIVE;
    }

    // 닉네임 업데이트 메서드
    public void updateNickname(String newNickname) {
        this.nickname = newNickname;
    }

    // 비밀번호 업데이트 메서드
    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }
    public void addKakaoInfo(Long kakaoId){
        this.oauthId=kakaoId;
        this.oauthType="kakao";
    }


    public MemberResponseDTO toDto() {
        return MemberResponseDTO.builder()
                .id(this.id)
                .email(this.email)
                .password(this.password)
                .oauthId(this.oauthId)
                .oauthType(this.oauthType)
                .nickname(this.nickname)
                .deletedAt(this.deletedAt)
                .status(this.status)
                .build();
    }
}
