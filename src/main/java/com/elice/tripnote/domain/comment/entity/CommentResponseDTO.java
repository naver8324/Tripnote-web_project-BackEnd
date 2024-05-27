package com.elice.tripnote.domain.comment.entity;


import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDTO {

    private Long id;
    private String content;
    private int report;
    private boolean isDeleted;
}
