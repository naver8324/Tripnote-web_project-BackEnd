package com.elice.tripnote.domain.post.controller;


import com.elice.tripnote.domain.post.entity.ImageRequestDTO;
import com.elice.tripnote.domain.post.entity.ImageResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;




@Tag(name = "Image API", description = "이미지 API입니다.")
public interface SwaggerImageController {






    @Operation(summary="이미지 저장 - 유저", description= "이미지를 저장할 때 사용합니다. 메타데이터를 받아 presigned url을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이미지 저장을 할 수 있는 presigned url을 받는데 성공했습니다.",  content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "<p>파일 크기가 범위를 넘었습니다.</p><br><p>이미지가 아닌 파일입니다.</p>",  content = @Content(mediaType = "application/json")),
    })
    ResponseEntity<ImageResponseDTO> createPresignedImageUrl(@Valid ImageRequestDTO imageDTO);



    @Operation(summary="이미지 검색 - 유저", description= "이미지를 찾을 때 사용합니다. key값을 받아서 public url을 반환합니다. 사실 이 메서드는 굳이 사용하지 않아도 됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이미지를 볼 수 있는 public url을 받는데 성공했습니다.",  content = @Content(mediaType = "text/plain")),
    })
    ResponseEntity<String> getImageUrl(String key);


}
