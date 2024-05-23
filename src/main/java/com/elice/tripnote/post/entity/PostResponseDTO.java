package com.elice.tripnote.post.entity;


import com.elice.tripnote.comment.entity.Comment;
import com.elice.tripnote.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostResponseDTO{

    private Long id;

    private String title;

    private String content;

    private int likes;

    private int report;

    private boolean isDeleted;

}
