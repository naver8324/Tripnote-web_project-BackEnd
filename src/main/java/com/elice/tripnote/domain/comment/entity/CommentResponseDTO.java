package com.elice.tripnote.domain.comment.entity;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDTO {

    private Long id;

    @NotNull
    private String content;

    @PositiveOrZero
    private int report;

    @NotNull
    private boolean isDeleted;
}
