package com.elice.tripnote.domain.route.controller;

import com.elice.tripnote.domain.route.entity.*;
import com.elice.tripnote.domain.route.service.RouteService;
import com.elice.tripnote.domain.spot.constant.Region;
import com.elice.tripnote.global.annotation.MemberRole;
import com.elice.tripnote.global.entity.PageRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RouteController implements SwaggerRouteController {
    private final RouteService routeService;

    /**
     * 경로 생성
     *
     * @param requestDto 총 경비, 경로 이름, 여행지 id 리스트(NotEmpty), 해시태그 id 리스트
     * @return 생성된 경로 id
     */
    @Override
    @MemberRole
    @PostMapping("/member/routes")
    public ResponseEntity<Long> save(@RequestBody SaveRequestDTO requestDto) {
        return ResponseEntity.ok(routeService.save(requestDto));
    }


//    /**
//     * 경로 공개/비공개
//     *
//     * @return 공개 여부를 변경하려는 경로 id
//     */
//    @Override
//    @MemberRole
//    @PatchMapping("/member/routes/status/{routeId}")
//    public ResponseEntity<Long> setRouteStatus(@PathVariable("routeId") Long routeId) {
//        return ResponseEntity.ok(routeService.setRouteToStatus(routeId));
//    }

    /**
     * 경로 삭제
     *
     * @return '삭제 상태'로 변경된 경로 id
     */
    @Override
    @MemberRole
    @DeleteMapping("/member/routes/{routeId}")
    public ResponseEntity<Long> deleteRoute(@PathVariable("routeId") Long routeId) {
        return ResponseEntity.ok(routeService.deleteRoute(routeId));
    }


    @MemberRole
    @GetMapping("/member/routes/{routeId}")
    public ResponseEntity<RecommendedRouteResponseDTO> getRouteInfo(@PathVariable("routeId") Long routeId) {
        return ResponseEntity.ok(routeService.getRouteInfo(routeId));
    }

    /**
     * 특정 지역 내에서 여행하는 경로(지역 기반 경로 추천) (회원)
     * 지역에 맞는 경로 알아내기
     *
     * @param region
     * @return 해당하는 경로들의 id 리스트
     */
    @Override
    @MemberRole
    @GetMapping("/member/routes/region")
    public ResponseEntity<List<RecommendedRouteResponseDTO>> getRegion(@RequestParam("region") String region) {
        Region status = Region.englishToRegion(region);
        return ResponseEntity.ok(routeService.getRegionMember(status));

    }

    /**
     * 특정 지역 내에서 여행하는 경로(지역 기반 경로 추천) (비회원)
     */
    @Override
    @GetMapping("/guest/routes/region")
    public ResponseEntity<List<RecommendedRouteResponseDTO>> getRegionGuest(@RequestParam("region") String region) {
        Region status = Region.englishToRegion(region);
        return ResponseEntity.ok(routeService.getRegionGuest(status));
    }

    // 여행지 선택했을 때, 해당 여행지를 지나가는 경로 id 리턴

    /**
     * 특정 여행지를 지나가는 경로(여행지 기반 경로 추천) (회원)
     *
     * @param spots
     * @return
     */
    @Override
    @MemberRole
    @GetMapping("/member/routes/spot")
    public ResponseEntity<List<RecommendedRouteResponseDTO>> getRoutesThroughSpot(@RequestParam(value = "spots", required = false) List<Long> spots) {
        return ResponseEntity.ok(routeService.getRoutesThroughSpotMember(spots));
    }

    /**
     * 특정 여행지를 지나가는 경로(여행지 기반 경로 추천) 비회원)
     */
    @Override
    @GetMapping("/guest/routes/spot")
    public ResponseEntity<List<RecommendedRouteResponseDTO>> getRoutesThroughSpotGuest(
            @RequestParam(value = "spots", required = false) List<Long> spots) {
        return ResponseEntity.ok(routeService.getRoutesThroughSpotGuest(spots));
    }

    /**
     * 특정 경로의 여행지 리스트 반환
     *
     * @param routeId 여행지 리스트가 궁금한 경로의 id
     * @return 특정 경로의 여행지 리스트
     */
    @Override
    @GetMapping("/member/routes/{routeId}/spots")
    public ResponseEntity<List<SpotResponseDTO>> getSpots(@PathVariable("routeId") Long routeId) {
        return ResponseEntity.ok(routeService.getSpots(routeId));
    }


    /**
     * 좋아요 추가/취소
     *
     * @param integratedRouteId 좋아요하고 싶은 경로 ic
     */
    @MemberRole
    @PatchMapping("/member/routes/like/{integratedRouteId}")
    public ResponseEntity<Void> addOrRemoveLike(@PathVariable("integratedRouteId") Long integratedRouteId) {
        routeService.addOrRemoveLike(integratedRouteId);
        return ResponseEntity.ok().build();
    }


    /**
     * 북마크 추가/취소
     *
     * @param integratedRouteId 북마크하고 싶은 경로 id
     */
    @MemberRole
    @PatchMapping("/member/routes/bookmark/{integratedRouteId}")
    public ResponseEntity<Void> addOrRemoveBookmark(@PathVariable("integratedRouteId") Long integratedRouteId) {
        routeService.addOrRemoveBookmark(integratedRouteId);
        return ResponseEntity.ok().build();
    }



    /**
     * 자신이 북마크한 경로 리스트
     *
     * @return [경로 id, 경로 이름, 해당되는 경로의 여행지 리스트] 리스트 리턴
     */
    @MemberRole
    @GetMapping("/member/routes/bookmark")
    public ResponseEntity<Page<RouteDetailResponseDTO>> findBookmark(PageRequestDTO pageRequestDTO) {
        //pageable 사용법
        //request param으로 page, size 조절 가능
        return ResponseEntity.ok(routeService.findBookmark(pageRequestDTO));
    }

    /**
     * 자신이 생성한 경로 리스트
     *
     * @return [경로 id, 경로 이름, 해당되는 경로의 여행지 리스트] 리스트 리턴
     */
    @MemberRole
    @GetMapping("/member/routes")
    public ResponseEntity<Page<RouteDetailResponseDTO>> findMyRoute(PageRequestDTO pageRequestDTO) {
        return ResponseEntity.ok(routeService.findMyRoute(pageRequestDTO));
    }


    /**
     * 경로 이름 수정
     *
     * @param requestDto 경로 id, 새로운 경로 이름
     */
    @MemberRole
    @PatchMapping("/member/routes/name")
    public ResponseEntity<Void> updateName(@RequestBody UpdateRouteNameRequestDTO requestDto) {
        routeService.updateName(requestDto);
        return ResponseEntity.ok().build();
    }
}
