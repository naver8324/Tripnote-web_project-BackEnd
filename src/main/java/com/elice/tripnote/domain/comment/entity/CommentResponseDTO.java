package com.elice.tripnote.domain.comment.entity;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CommentResponseDTO {

    private Long id;
    private String content;
    private int report;
    private boolean isDeleted;
}
