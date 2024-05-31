package com.elice.tripnote.domain.route.controller;

import com.elice.tripnote.domain.integratedroute.status.IntegratedRouteStatus;
import com.elice.tripnote.domain.route.entity.LikeBookmarkResponseDTO;
import com.elice.tripnote.domain.route.entity.SaveRequestDTO;
import com.elice.tripnote.domain.route.entity.SpotResponseDTO;
import com.elice.tripnote.domain.route.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member/routes")
public class RouteController implements SwaggerRouteController {
    private final RouteService routeService;

    /**
     * 경로 생성
     *
     * @param requestDto 경로를 만드는 유저 id, 총 경비, 여행지 id 리스트, 해시태그 id 리스트
     * @return 생성된 경로 id
     */
    @Override
    @PostMapping
    public ResponseEntity<Long> save(@RequestBody SaveRequestDTO requestDto) {
        return ResponseEntity.ok(routeService.save(requestDto));
    }

    /**
     * 경로 비공개
     *
     * @return 비공개 처리된 경로 id
     */
    @Override
    @PatchMapping("/private/{routeId}")
    public ResponseEntity<Long> setRouteToPrivate(@PathVariable("routeId") Long routeId) {
        return ResponseEntity.ok(routeService.setRouteToPrivate(routeId));
    }

    /**
     * 경로 공개
     *
     * @return 공개 처리된 경로 id
     */
    @Override
    @PatchMapping("/public/{routeId}")
    public ResponseEntity<Long> setRouteToPublic(@PathVariable("routeId") Long routeId) {
        return ResponseEntity.ok(routeService.setRouteToPublic(routeId));
    }

    /**
     * 경로 삭제
     *
     * @return '삭제 상태'로 변경된 경로 id
     */
    @Override
    @DeleteMapping("/{routeId}")
    public ResponseEntity<Long> deleteRoute(@PathVariable("routeId") Long routeId) {
        return ResponseEntity.ok(routeService.deleteRoute(routeId));
    }

    /**
     * 지역 선택 후 경로 추천하는 페이지
     * 지역에 맡는 경로 알아내기
     * @param region
     * @param hashtags 설정한 해시태그의 id
     * @return 해당하는 경로들의 id 리스트
     */
    @Override
    @GetMapping("/region")
    public ResponseEntity<List<Long>> getRegion(@RequestParam("region") String region,
                                                          @RequestParam(value = "hashtags", required = false) List<Long> hashtags) {
        if (hashtags == null) hashtags = Collections.emptyList();
        IntegratedRouteStatus status = IntegratedRouteStatus.fromName(region);
        return ResponseEntity.ok(routeService.getRegion(status, hashtags));
    }

    // 경로 id 리스트 보내면
    // 각 경로에 맞는 여행지 리스트 리턴
    @Override
    @GetMapping("/{routeId}/spots")
    public ResponseEntity<List<SpotResponseDTO>> getSpots(@PathVariable("routeId") Long integratedRouteId){
        return ResponseEntity.ok(routeService.getSpots(integratedRouteId));
    }


    // 경로 id 리스트 보내면 각 경로의 하트 수, 북마크 수 리턴
    @Override
    @GetMapping("/{routeId}/like-bookmark")
    public ResponseEntity<LikeBookmarkResponseDTO> getLikeBookmark(@PathVariable("routeId") Long integratedRouteId){
        return ResponseEntity.ok(routeService.getLikeBookmark(integratedRouteId));
    }

    // 여행지 선택했을 때, 해당 여행지를 지나가는 경로 id 리턴
    @GetMapping("/spot")
    public ResponseEntity<List<Long>> getRoutesThroughSpot(@RequestParam(value = "hashtags", required = false) List<Long> hashtags,
                                                           @RequestParam(value = "spots", required = false) List<Long> spots){
        return ResponseEntity.ok(routeService.getRoutesThroughSpot(hashtags, spots));
    }
}
