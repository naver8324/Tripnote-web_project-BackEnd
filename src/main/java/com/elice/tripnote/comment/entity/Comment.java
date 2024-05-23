package com.elice.tripnote.comment.entity;


import com.elice.tripnote.global.entity.BaseTimeEntity;
import com.elice.tripnote.user.entity.User;
import com.elice.tripnote.post.entity.Post;
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

    @ColumnDefault("false")
    private boolean isDeleted;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int report;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;


//    @Builder
//    private Comment(String content, int report, Post post) {
//        this.content = content;
//        this.report = report;
//        this.post = post;
//    }


    public void updateContent(CommentRequestDTO commentDTO){
        this.content = commentDTO.getContent();
    }

    public void addReport(){
        report++;
    }
    public void removeReport(){
        report--;
    }

    public void delete(){
        isDeleted = true;
    }

    public CommentResponseDTO toDTO() {

        return CommentResponseDTO.builder().id(id).content(content).report(report).build();

    }
}
