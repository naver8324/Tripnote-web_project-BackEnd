package com.elice.tripnote.domain.member.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class KakaoInfoDTO {
    private Long kakaoId;
    private String email;
    private String nickName;
}
