package com.elice.tripnote.domain.mail.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequestDTO {
    @Email
    @NotEmpty(message = "이메일을 입력해 주세요")
    private String email;
}