package com.elice.tripnote.domain.spot.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SpotDTO {
//    private String title;                   // 음식명, 장소명
//    private String category;                // 카테고리
//    private String address;                 // 주소
//    private String roadAddress;             // 도로명
////    private String homePageLink;            // 홈페이지 주소
//    private String imageLink;               // 음식, 가게 이미지 주소
// //   private boolean isVisit;                // 방문 여부
//    private int visitCount;                 // 방문 횟수
// //   private LocalDateTime lastVisitDate;    // 마지막 방문 일자
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
