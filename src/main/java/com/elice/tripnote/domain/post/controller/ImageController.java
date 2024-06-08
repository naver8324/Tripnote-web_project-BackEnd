package com.elice.tripnote.domain.post.controller;


import com.elice.tripnote.domain.post.entity.ImageRequestDTO;
import com.elice.tripnote.domain.post.entity.ImageResponseDTO;
import com.elice.tripnote.global.annotation.MemberRole;
import com.elice.tripnote.global.exception.CustomException;
import com.elice.tripnote.global.exception.ErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ImageController implements SwaggerImageController {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final S3Client client;
    private final S3Presigner presigner;


    @Override
    @MemberRole
    @PutMapping("/api/member/images")
    public ResponseEntity<ImageResponseDTO> createPresignedImageUrl(@Valid @RequestBody ImageRequestDTO imageDTO){


        // file size 10MB 이하로 제한
        if(10485760L < imageDTO.getContentLength()){
            throw new CustomException(ErrorCode.EXCEED_SIZE_LIMIT);
        }

        // 이미지 파일만 업로드 가능
        if(!imageDTO.getContentType().startsWith("image")){
            throw new CustomException(ErrorCode.NOT_MATCHED_TYPE);
        }

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String formattedNow = now.format(formatter);

        String key = formattedNow + "/" + UUID.randomUUID().toString() + imageDTO.getFileName();


        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(imageDTO.getContentType())
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(1)) // The URL expires in 1 minutes.
                .putObjectRequest(objectRequest)
                .build();


        PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(presignRequest);
        String myURL = presignedRequest.url().toString();
        log.info("Presigned URL to upload a file to: [{}]", myURL);
        log.info("HTTP method: [{}]", presignedRequest.httpRequest().method());


        ImageResponseDTO imageResponseDTO = ImageResponseDTO.builder()
                .presignedUrl(presignedRequest.url().toExternalForm())
                .key(key)
                .build();


        return ResponseEntity.ok().body(imageResponseDTO);
    }

    @Override
    @MemberRole
    @GetMapping("/api/member/images")
    public ResponseEntity<String> getImageUrl(@RequestParam(name = "key") String key){


        String url = client.utilities()
                    .getUrl(builder -> builder
                            .bucket(bucket)
                            .key(key))
                    .toExternalForm();


        return ResponseEntity.ok().body(url);

    }








}
