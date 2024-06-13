package com.elice.tripnote.domain.mail.controller;

import com.elice.tripnote.domain.mail.entity.EmailCheckDTO;
import com.elice.tripnote.domain.mail.entity.EmailRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Mail API", description = "메일 인증 API입니다.")
public interface SwaggerMailController {

    @Operation(summary = "메일 인증 코드 전송", description = "이메일로 인증 코드를 전송합니다.")
    @ApiResponse(responseCode = "200", description = "메일 전송에 성공하였습니다.")
    @PostMapping("/sendmail")
    ResponseEntity<Void> mailSend(@RequestBody @Parameter(description = "메일 요청 DTO", required = true) EmailRequestDTO emailDto);


    @Operation(summary = "메일 인증 코드 확인", description = "이메일로 전송된 인증 코드를 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증 코드 확인에 성공하였습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "해당하는 인증 코드가 존재하지 않습니다.", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/checkmail")
    ResponseEntity<Boolean> mailCheck(@RequestBody @Parameter(description = "메일 확인 DTO", required = true) EmailCheckDTO emailCheckDto);

    @Operation(summary = "비밀번호 재설정 요청", description = "이메일로 비밀번호 재설정을 요청합니다.")
    @ApiResponse(responseCode = "200", description = "비밀번호 재설정에 성공하였습니다.")
    @PostMapping("/reset-password")
    ResponseEntity<Void> resetPassword(@RequestBody @Parameter(description = "비밀번호 재설정 요청 DTO", required = true) EmailCheckDTO passwordResetRequestDTO);
}