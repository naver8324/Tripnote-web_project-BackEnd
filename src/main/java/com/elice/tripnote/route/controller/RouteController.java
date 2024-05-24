package com.elice.tripnote.route.controller;

import com.elice.tripnote.route.dto.SaveRequestDto;
import com.elice.tripnote.route.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/routes")
public class RouteController implements SwaggerRouteController{
    private final RouteService routeService;

    /**
     * 경로 생성
     * @param requestDto
     * @return
     */
    @Override
    @PostMapping
    public ResponseEntity<Long> save(@RequestBody SaveRequestDto requestDto) {
        return ResponseEntity.ok(routeService.save(requestDto));
    }
}
