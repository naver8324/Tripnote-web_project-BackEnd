package com.elice.tripnote.domain.post.entity;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDTO{

    private Long id;

    private String title;

    private String content;

    private boolean isDeleted;


}
