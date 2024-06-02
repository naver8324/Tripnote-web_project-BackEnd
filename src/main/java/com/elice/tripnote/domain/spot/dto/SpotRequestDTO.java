package com.elice.tripnote.domain.spot.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class SpotRequestDTO {

    @NotBlank(message = "여행지명은 필수 항복입니다.")
    private String location;

    @NotBlank(message = "좋아요는 필수 항복입니다.")
    @Min(1)
    private int likes;

    @NotBlank(message = "이미지 url은 필수 항복입니다.")
    private String imageUrl;

    @NotBlank(message = "지역은 필수 항복입니다.")
    private String region; //지역
}
