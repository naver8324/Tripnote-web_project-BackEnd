package com.elice.tripnote.domain.post.entity;


import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostRequestDTO {

    private Long id;

    private String title;

    private String content;

}
