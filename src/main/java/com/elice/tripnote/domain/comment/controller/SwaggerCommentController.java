package com.elice.tripnote.domain.comment.controller;


import com.elice.tripnote.domain.comment.entity.CommentRequestDTO;
import com.elice.tripnote.domain.comment.entity.CommentResponseDTO;
import com.elice.tripnote.global.entity.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

@Tag(name = "Comment API", description = "댓글 API입니다.")
public interface SwaggerCommentController {


    @Operation(summary="게시글 댓글 조회 - 유저", description= "유저가 게시글에 해당하는 댓글을 조회할 때 사용하는 api입니다. 삭제되지 않은 댓글만 조회 가능합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 조회에 성공하였습니다.",  content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "댓글에 해당하는 게시판이 존재하지 않습니다.",  content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Parameters(value = {
            @Parameter(name="postId", description = "게시글 번호", example = "1123"),
            @Parameter(name="page", description = "페이지 번호", example = "5"),
            @Parameter(name="size", description = "페이지 크기", example = "30")
    })

    ResponseEntity<Page<CommentResponseDTO>> getCommentsByPostId(Long postId, int page, int size);


    @Operation(summary="게시글 댓글 조회 - 관리자", description= "관리자가 모든 댓글을 조회할 때 사용하는 api입니다. 삭제된 댓글도 조회 가능합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 조회에 성공하였습니다.", content = @Content(mediaType = "application/json")),
    })
    @Parameters(value = {
            @Parameter(name="page", description = "페이지 번호", example = "5"),
            @Parameter(name="size", description = "페이지 크기", example = "30")
    })

    ResponseEntity<Page<CommentResponseDTO>> getCommentsAll(int page, int size);


    @Operation(summary="게시글 유저 댓글 조회 - 관리자", description= "관리자가 유저의 모든 댓글을 조회할 때 사용하는 api입니다. 삭제된 댓글도 조회 가능합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 조회에 성공하였습니다.",  content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "해당하는 유저는 존재하지 않습니다.",  content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Parameters(value = {
            @Parameter(name="memberId", description = "멤버 번호", example = "332"),
            @Parameter(name="page", description = "페이지 번호", example = "5"),
            @Parameter(name="size", description = "페이지 크기", example = "30")
    })
    ResponseEntity<Page<CommentResponseDTO>> getCommentsByMemberId(Long memberId, int page, int size);


    @Operation(summary="게시글 유저 댓글 생성 - 유저", description= "유저의 댓글을 생성할 때 사용하는 api입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "댓글 생성에 성공하였습니다.",  content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "<p>해당하는 유저는 존재하지 않습니다.</p><br><p>해당하는 게시글은 존재하지 않습니다.</p>",  content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
    })
    @Parameters(value = {
            @Parameter(name="postId", description = "게시글 번호", example = "1356"),
    })
    ResponseEntity<CommentResponseDTO> saveComment(CommentRequestDTO commentDTO, Long postId);



    @Operation(summary="게시글 유저 댓글 수정 - 유저", description= "유저의 댓글을 수정할 때 사용하는 api입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 수정에 성공하였습니다.",  content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "해당하는 댓글은 존재하지 않습니다.",  content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "해당 댓글을 수정할 권한이 존재하지 않습니다.",  content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
    })
    @Parameters(value = {
            @Parameter(name="commentId", description = "댓글 번호", example = "1356"),
    })
    ResponseEntity<CommentResponseDTO> updateComment(CommentRequestDTO commentDTO, Long commentId);


    @Operation(summary="게시글 댓글 신고 - 유저", description= "댓글을 신고할 때 사용하는 api입니다. 다시 누르면 신고를 해제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 신고에 성공하였습니다.",  content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "<p>해당하는 댓글은 존재하지 않습니다.</p><br><p>해당하는 유저는 존재하지 않습니다.</p>",  content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
    })
    @Parameters(value = {
            @Parameter(name="commentId", description = "댓글 번호", example = "1356"),
    })
    ResponseEntity reportComment(Long commentId);


    @Operation(summary="게시글 댓글 삭제 - 유저", description= "댓글을 삭제할 때 사용하는 api입니다. 댓글을 쓴 유저가 사용합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "댓글 삭제에 성공하였습니다.",  content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "해당 댓글을 삭제할 권한이 존재하지 않습니다.",  content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
    })
    @Parameters(value = {
            @Parameter(name="commentId", description = "댓글 번호", example = "1356"),
    })
    ResponseEntity deleteComment(Long commentId);




    @Operation(summary="게시글 댓글 삭제 - 관리자", description= "댓글을 삭제할 때 사용하는 api입니다. 관리자가 사용합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "댓글 삭제에 성공하였습니다.",  content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "해당하는 댓글은 존재하지 않습니다.",  content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
    })
    @Parameters(value = {
            @Parameter(name="commentId", description = "댓글 번호", example = "1356"),
    })
    ResponseEntity deleteCommentAdmin(Long commentId);












}
