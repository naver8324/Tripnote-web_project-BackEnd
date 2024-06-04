package com.elice.tripnote.domain.spot.dto;

import com.elice.tripnote.domain.spot.constant.Region;
import jakarta.persistence.Column;
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

    @NotBlank(message = "여행지명은 필수 항복입니다.")
    private String location;

    private String imageUrl;

    @NotBlank(message = "지역은 필수 항복입니다.")
    private Region region; //지역

    @NotBlank(message = "주소는 필수 항복입니다.")
    private String address; //주소

    @NotBlank(message = "위도는 필수 항복입니다.")
    private double lat; //위도

    @NotBlank(message = "경도는 필수 항복입니다.")
    private double lng; //경도
}
