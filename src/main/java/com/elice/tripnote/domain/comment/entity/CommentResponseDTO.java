package com.elice.tripnote.domain.comment.entity;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDTO {

    @NotNull
    private Long id;


    @NotBlank
    @Max(20)
    private String nickname;

    @NotNull
    private String content;

    @NotNull
    private LocalDateTime createdAt;

    @PositiveOrZero
    private int report;

    @NotNull
    private boolean isDeleted;
}
