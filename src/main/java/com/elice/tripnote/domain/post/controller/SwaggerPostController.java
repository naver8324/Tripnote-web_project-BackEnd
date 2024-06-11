package com.elice.tripnote.domain.post.controller;


import com.elice.tripnote.domain.hashtag.entity.HashtagRequestDTO;
import com.elice.tripnote.domain.post.entity.PostDetailResponseDTO;
import com.elice.tripnote.domain.post.entity.PostRequestDTO;
import com.elice.tripnote.domain.post.entity.PostResponseDTO;
import com.elice.tripnote.global.entity.ErrorResponse;
import com.elice.tripnote.global.entity.PageRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Post API", description = "게시글 API입니다.")
public interface SwaggerPostController {


    @Operation(summary="게시글 조회 - 모두", description= "게시글을 조회할 때 사용하는 api입니다. 삭제되지 않은 게시글만 조회 가능합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 조회에 성공하였습니다.",  content = @Content(mediaType = "application/json")),
    })
    ResponseEntity<Page<PostResponseDTO>> getPosts(@Valid PageRequestDTO pageRequestDTO);


    @Operation(summary="해쉬태그 게시글 조회  - 모두", description= "특정 해쉬태그들을 가진 게시글을 조회할 때 사용하는 api입니다. 삭제되지 않은 게시글만 조회 가능합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 조회에 성공하였습니다.",  content = @Content(mediaType = "application/json")),
    })
    ResponseEntity<Page<PostResponseDTO>> getPostsByHashtag(@Valid List<HashtagRequestDTO> hashtagRequestDTOList, PageRequestDTO pageRequestDTO);


    @Operation(summary="게시글 조회 - 유저", description= "유저가 자기가 쓴 모든 게시글을 조회할 때 사용하는 api입니다. 삭제되지 않은 게시글만 조회 가능합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 조회에 성공하였습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "해당하는 유저는 존재하지 않습니다.",  content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<Page<PostResponseDTO>> getPostsByMemberId(@Valid PageRequestDTO pageRequestDTO);


    @Operation(summary="좋아요 게시글 조회 - 유저", description= "유저가 좋아요한 게시글을 조회할 때 사용하는 api입니다. 삭제되지 않은 게시글만 조회 가능합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 조회에 성공하였습니다.",  content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "해당하는 유저는 존재하지 않습니다.",  content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<Page<PostResponseDTO>> getPostsByMemberWithLikes(@Valid PageRequestDTO pageRequestDTO);


    @Operation(summary="북마크 게시글 조회 - 유저", description= "유저가 북마크한 게시글을 조회할 때 사용하는 api입니다. 삭제되지 않은 게시글만 조회 가능합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 조회에 성공하였습니다.",  content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "해당하는 유저는 존재하지 않습니다.",  content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<Page<PostResponseDTO>> getPostsByMemberWithMark(@Valid PageRequestDTO pageRequestDTO);


    @Operation(summary="게시글 조회 - 관리자", description= "게시글을 조회할 때 사용하는 api입니다. 삭제된 게시글도 조회 가능합니다. 게시글 번호가 있으면 특정 게시글을 쓴 멤버의 게시글을 전체 조회합니다. 닉네임이 있으면 닉네임이 일치하는 게시글을 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 조회에 성공하였습니다.",  content = @Content(mediaType = "application/json")),
    })
    @Parameters(value = {
            @Parameter(name="postId", description = "게시글 번호", example = "1"),
            @Parameter(name="nickname", description = "유저 닉네임", example = "user1")
    })

    ResponseEntity<Page<PostResponseDTO>> getPostsAll(Long postId, String nickname, @Valid PageRequestDTO pageRequestDTO);



    @Operation(summary="게시글 상세 조회 - 유저", description= "게시글을 상세 조회할 때 사용하는 api입니다. 삭제되지 않은 게시글만 조회 가능합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 조회에 성공하였습니다.",  content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "<p>해당하는 유저는 존재하지 않습니다.</p><br><p>해당하는 게시글은 존재하지 않습니다.</p>",  content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Parameters(value = {
            @Parameter(name="postId", description = "게시글 번호", example = "112"),
    })
    ResponseEntity<PostDetailResponseDTO> getPost(Long postId);


    @Operation(summary="게시글 생성 - 유저", description= "유저가 게시글을 생성할 때 사용하는 api입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "게시글 생성에 성공하였습니다.",  content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "이 경로는 비공개, 삭제되었거나 유저의 경로가 아닙니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "<p>해당하는 유저는 존재하지 않습니다.</p><br><p>해당하는 게시글은 존재하지 않습니다.</p><br><p>해당하는 경로는 존재하지 않습니다.</p>",  content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
    })
    @Parameters(value = {
            @Parameter(name="routeId", description = "경로 번호", example = "1126"),
    })
    ResponseEntity<PostDetailResponseDTO> savePost(@Valid PostRequestDTO postDTO, Long routeId);



    @Operation(summary="게시글 수정 - 유저", description= "유저의 게시글을 수정할 때 사용하는 api입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 수정에 성공하였습니다.",  content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "해당하는 게시글은 존재하지 않습니다.",  content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "해당 게시글을 수정할 권한이 존재하지 않습니다.",  content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
    })
    @Parameters(value = {
            @Parameter(name="postId", description = "게시글 번호", example = "1356"),
    })
    ResponseEntity<PostDetailResponseDTO> updatePost(@Valid PostRequestDTO postDTO, Long postId);



    @Operation(summary="게시글 좋아요 - 유저", description= "게시글을 좋아요 할 때 사용하는 api입니다. 다시 누르면 좋아요를 해제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 좋아요에 성공하였습니다.",  content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "<p>해당하는 게시글은 존재하지 않습니다.</p><br><p>해당하는 유저는 존재하지 않습니다.</p>",  content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
    })
    @Parameters(value = {
            @Parameter(name="postId", description = "게시글 번호", example = "1356"),
    })
    ResponseEntity likePost(Long postId);


    @Operation(summary="게시글 북마크 - 유저", description= "게시글을 북마크 할 때 사용하는 api입니다. 다시 누르면 북마크를 해제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 북마크에 성공하였습니다.",  content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "<p>해당하는 게시글은 존재하지 않습니다.</p><br><p>해당하는 유저는 존재하지 않습니다.</p>",  content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
    })
    @Parameters(value = {
            @Parameter(name="postId", description = "게시글 번호", example = "1356"),
    })
    ResponseEntity markPost(Long postId);

    @Operation(summary="게시글 신고 - 유저", description= "게시글을 신고할 때 사용하는 api입니다. 다시 누르면 신고를 해제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 신고에 성공하였습니다.",  content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "<p>해당하는 게시글은 존재하지 않습니다.</p><br><p>해당하는 유저는 존재하지 않습니다.</p>",  content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
    })
    @Parameters(value = {
            @Parameter(name="postId", description = "게시글 번호", example = "1356"),
    })
    ResponseEntity reportPost(Long postId);


    @Operation(summary="게시글 삭제 - 유저", description= "게시글을 삭제할 때 사용하는 api입니다. 게시글을 쓴 유저가 사용합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "게시글 삭제에 성공하였습니다.",  content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "해당 게시글을 삭제할 권한이 존재하지 않습니다.",  content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
    })
    @Parameters(value = {
            @Parameter(name="postId", description = "게시글 번호", example = "1356"),
    })
    ResponseEntity deletePost(Long postId);


    @Operation(summary="게시글 삭제 - 관리자", description= "게시글을 삭제할 때 사용하는 api입니다. 관리자가 사용합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "게시글 삭제에 성공하였습니다.",  content = @Content(mediaType = "application/json")),
    })
    @Parameters(value = {
            @Parameter(name="postId", description = "게시글 번호", example = "1356"),
    })
    ResponseEntity deletePostAdmin(Long postId);






}
