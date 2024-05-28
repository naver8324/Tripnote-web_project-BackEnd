package com.elice.tripnote.domain.post.entity;


import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostRequestDTO {


    private String title;

    private String content;

}
