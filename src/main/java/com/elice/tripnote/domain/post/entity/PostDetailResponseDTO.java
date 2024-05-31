package com.elice.tripnote.domain.post.entity;


import com.elice.tripnote.domain.route.entity.Route;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDetailResponseDTO {

    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String content;


    @PositiveOrZero
    private int likes;

    @PositiveOrZero
    private int report;


    @NotBlank
    private boolean isDeleted;

    private LocalDateTime likedAt;

    private LocalDateTime reportedAt;


    @NotNull
    private Long routeId;

}
