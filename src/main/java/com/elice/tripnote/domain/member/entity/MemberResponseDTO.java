package com.elice.tripnote.domain.member.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDTO {

    private Long id;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    private Long oauthId;

    private String oauthType;

    @NotBlank
    private String nickname;

    private LocalDateTime deletedAt;

    @NotNull
    private Status status;


}
