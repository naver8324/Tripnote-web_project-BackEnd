package com.elice.tripnote.domain.member.entity;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponseDTO {
    private String kakaoToken;
    private String jwtToken;
}
