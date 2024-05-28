package com.elice.tripnote.domain.route.controller;

import com.elice.tripnote.domain.integratedroute.status.IntegratedRouteStatus;
import com.elice.tripnote.domain.route.entity.GetRegionResponseDTO;
import com.elice.tripnote.domain.route.entity.SaveRequestDTO;
import com.elice.tripnote.domain.route.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/routes")
public class RouteController implements SwaggerRouteController{
    private final RouteService routeService;

    /**
     * 경로 생성
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
     * @return 비공개 처리된 경로 id
     */
    @Override
    @PatchMapping("/private/{routeId}")
    public ResponseEntity<Long> setRouteToPrivate(@PathVariable("routeId") Long routeId) {
        return ResponseEntity.ok(routeService.setRouteToPrivate(routeId));
    }

    /**
     * 경로 공개
     * @return 공개 처리된 경로 id
     */
    @Override
    @PatchMapping("/public/{routeId}")
    public ResponseEntity<Long> setRouteToPublic(@PathVariable("routeId") Long routeId) {
        return ResponseEntity.ok(routeService.setRouteToPublic(routeId));
    }

    /**
     * 경로 삭제
     * @return '삭제 상태'로 변경된 경로 id
     */
    @Override
    @DeleteMapping("/{routeId}")
    public ResponseEntity<Long> deleteRoute(@PathVariable("routeId") Long routeId) {
        return ResponseEntity.ok(routeService.deleteRoute(routeId));
    }

    /**
     * 지역 선택 후 경로 추천하는 페이지 불러오기
     * @return
     */
    @Override
    @GetMapping("/region")
    //TODO: 추후 해시태그를 어떻게 관리할지 결정되면 해시 태그 입력 받는 것도 추가
    public ResponseEntity<GetRegionResponseDTO> getRegion(@RequestParam("region") String region) {
        IntegratedRouteStatus status = IntegratedRouteStatus.fromName(region);
        return ResponseEntity.ok(routeService.getRegion(status));
    }
}
