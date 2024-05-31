package com.elice.tripnote.domain.comment.entity;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDTO {

    @NotBlank
    private String content;


}
