package com.elice.tripnote.domain.post.entity;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDTO{

    private Long id;


    @NotBlank
    private String title;


    @NotBlank
    private String content;

    @NotNull
    private boolean isDeleted;


}
