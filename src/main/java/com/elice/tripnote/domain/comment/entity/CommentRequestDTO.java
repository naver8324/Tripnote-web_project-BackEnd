package com.elice.tripnote.domain.comment.entity;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CommentRequestDTO {

    private Long id;
    private String content;
}
