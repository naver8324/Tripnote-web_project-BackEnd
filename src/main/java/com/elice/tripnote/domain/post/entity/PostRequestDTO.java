package com.elice.tripnote.domain.post.entity;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostRequestDTO {

    @NotBlank
    private String title;


    @NotBlank
    private String content;

}
