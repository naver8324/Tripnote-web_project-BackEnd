package com.elice.tripnote.domain.comment.entity;


import com.elice.tripnote.global.entity.BaseTimeEntity;
import com.elice.tripnote.domain.member.entity.Member;
import com.elice.tripnote.domain.post.entity.Post;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    // 사용하는 db에 따라 @Lob를 사용할 때는 큰 주의가 필요하다. postgre와 oracle은 특히 주의해야 한다.
    @Lob
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(columnDefinition = "TINYINT(1)")
    @ColumnDefault("false")
    private boolean isDeleted;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int report;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;


//    @Builder
//    private Comment(String content, int report, Post post) {
//        this.content = content;
//        this.report = report;
//        this.post = post;
//    }


    public void update(CommentRequestDTO commentDTO){
        this.content = commentDTO.getContent();
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

    public CommentResponseDTO toDTO() {

        return CommentResponseDTO.builder().content(content).report(report).isDeleted(isDeleted).build();

    }
}
