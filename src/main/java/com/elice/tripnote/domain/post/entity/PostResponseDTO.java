package com.elice.tripnote.domain.post.entity;


import com.elice.tripnote.domain.hashtag.entity.HashtagResponseDTO;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDTO{

    private Long id;


    @NotBlank
    private String title;


    @NotBlank
    private String content;

    @NotNull
    private boolean isDeleted;

    @NotNull
    private LocalDateTime createdAt;


    @NotBlank
    @Max(20)
    private String nickname;


    private List<HashtagResponseDTO> hashtagResponseDTOList;


    public PostResponseDTO(Long id, String title, String content, boolean isDeleted, LocalDateTime createdAt, String nickname) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.nickname = nickname;
    }
}
