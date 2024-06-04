package com.elice.tripnote.domain.spot.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Getter
public class SpotRequestDTO {

//    @NotBlank(message = "여행지명은 필수 항복입니다.")
//    private String location;
//
//    private String imageUrl;
//
//    @NotBlank(message = "지역은 필수 항복입니다.")
//    private String region; //지역
//
//    @NotBlank(message = "주소는 필수 항복입니다.")
//    private String address; //주소
//
//    @NotBlank(message = "위도는 필수 항복입니다.")
//    private double lat; //주소
//
//    @NotBlank(message = "경도는 필수 항복입니다.")
//    private double lng; //경도

    private List<Long> spotId;

    public List<Long> getSpotId() {
        return spotId;
    }

    public void setSpotId(List<Long> spotId) {
        this.spotId = spotId;
    }
}
