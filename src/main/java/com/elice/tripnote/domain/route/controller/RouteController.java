package com.elice.tripnote.domain.route.controller;

import com.elice.tripnote.domain.route.dto.SaveRequestDto;
import com.elice.tripnote.domain.route.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/routes")
public class RouteController implements SwaggerRouteController{
    private final RouteService routeService;

    /**
     * 경로 생성
     * @param requestDto 경로를 만드는 유저 id, 총 경비, 여행지 id 리스트, 해시태그 id 리스트
     * @return 생성된 경로 id
     */
    @Override
    @PostMapping
    public ResponseEntity<Long> save(@RequestBody SaveRequestDto requestDto) {
        return ResponseEntity.ok(routeService.save(requestDto));
    }

    @Override
    @PatchMapping("/private/{routeId}")
    public ResponseEntity<Long> setRouteToPrivate(@PathVariable("routeId") Long routeId) {
        return ResponseEntity.ok(routeService.setRouteToPrivate(routeId));
    }

    @Override
    @PatchMapping("/public/{routeId}")
    public ResponseEntity<Long> setRouteToPublic(@PathVariable("routeId") Long routeId) {
        return ResponseEntity.ok(routeService.setRouteToPublic(routeId));
    }

    @Override
    @DeleteMapping("/{routeId}") //실제로는 삭제하지 않고 '삭제 상태'로 변경하는 건데 delete?? patch...??
    public ResponseEntity<Long> deleteRoute(@PathVariable("routeId") Long routeId) {
        return ResponseEntity.ok(routeService.deleteRoute(routeId));
    }
}
