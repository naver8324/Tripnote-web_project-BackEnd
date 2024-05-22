package com.elice.tripnote.comment.entity;


import com.elice.tripnote.global.entity.BaseTimeEntity;
import com.elice.tripnote.post.entity.Post;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Builder
public class CommentDTO {

    private Long id;
    private String content;
    private int report;
}
