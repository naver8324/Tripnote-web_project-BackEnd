package com.elice.tripnote.member.entity;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class MemberRequestDTO {

    private String email;

    private String password;

    private String nickname;
}
