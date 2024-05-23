package com.elice.tripnote.comment.entity;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentResponseDTO {

    private Long id;
    private String content;
    private int report;
    private boolean isDeleted;
}
