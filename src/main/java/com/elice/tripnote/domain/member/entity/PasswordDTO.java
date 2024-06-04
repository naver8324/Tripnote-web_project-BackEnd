package com.elice.tripnote.domain.member.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class PasswordDTO {
    private String password;
}