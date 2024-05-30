package com.elice.tripnote.domain.post.controller;


import com.elice.tripnote.domain.post.entity.ImageRequestDTO;
import com.elice.tripnote.global.entity.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

public interface SwaggerImageController {






    @Operation(summary="이미지 저장 - 유저", description= "이미지를 저장할 때 사용합니다. 메타데이터를 받아 presigned url을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이미지 저장을 할 수 있는 presigned url을 받는데 성공했습니다.",  content = @Content(mediaType = "text/plain")),
    })
    ResponseEntity<String> createPresignedImageUrl(@RequestBody ImageRequestDTO imageDTO);


}
