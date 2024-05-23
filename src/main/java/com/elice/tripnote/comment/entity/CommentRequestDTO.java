package com.elice.tripnote.comment.entity;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentRequestDTO {

    private Long id;
    private String content;
}
