package com.elice.tripnote.domain.route.controller;

import com.elice.tripnote.domain.route.entity.*;
import com.elice.tripnote.domain.route.service.RouteService;
import com.elice.tripnote.domain.spot.constant.Region;
import jakarta.validation.Valid;
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
     * @param requestDto 경로를 만드는 유저 id, 총 경비, 여행지 id 리스트(NotEmpty), 해시태그 id 리스트
     * @return 생성된 경로 id
     */
    @Override
    @PostMapping
    public ResponseEntity<Long> save(@Valid @RequestBody SaveRequestDTO requestDto) {
        return ResponseEntity.ok(routeService.save(requestDto));
    }


    //TODO: 경로 공개/비공개 하나로 합치기

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
     * 특정 지역 내에서 여행하는 경로(지역 기반 경로 추천)
     * 지역에 맞는 경로 알아내기
     * @param region
     * @param hashtags 설정한 해시태그의 id
     * @return 해당하는 경로들의 id 리스트
     */
    @Override
    @GetMapping("/region")
    public ResponseEntity<List<RecommendedRouteResponseDTO>> getRegion(@RequestParam("region") String region,
                                                                       @RequestParam(value = "hashtags", required = false) List<Long> hashtags) {
        //TODO: 경로에 해시태그 안붙이기
        if (hashtags == null) hashtags = Collections.emptyList();
        Region status = Region.fromString(region);
        return ResponseEntity.ok(routeService.getRegion(status, hashtags));
        /*
        아래 값 5개
        {
           route id
          여행지 리스트 - 순서 정리된 채로, (id, region 필요 없음)
          likes: (해당 경로의 좋아요 개수)
          likedAt(경로 조회한 유저가 좋아요 눌렀는지)
          markedAt(경로 조회한 유저가 북마크를 눌렀는지)
        }

         */
    }

    // 여행지 선택했을 때, 해당 여행지를 지나가는 경로 id 리턴

    /**
     * 특정 여행지를 지나가는 경로(여행지 기반 경로 추천)
     * @param hashtags
     * @param spots
     * @return
     */
    @Override
    @GetMapping("/spot")
    public ResponseEntity<List<RecommendedRouteResponseDTO>> getRoutesThroughSpot(@RequestParam(value = "hashtags", required = false) List<Long> hashtags,
                                                                                  @RequestParam(value = "spots", required = false) List<Long> spots) {
        return ResponseEntity.ok(routeService.getRoutesThroughSpot(hashtags, spots));
        /*
        아래 값 5개
        {
           route id
          여행지 리스트 - 순서 정리된 채로, (id, region 필요 없음)
          likes: (해당 경로의 좋아요 개수)
          likedAt(경로 조회한 유저가 좋아요 눌렀는지)
          markedAt(경로 조회한 유저가 북마크를 눌렀는지)
        }

         */
    }

    /**
     * 특정 경로의 여행지 리스트 반환
     * 게시글에서 경로 보여줄 때 사용?? -> 통합경로가 아닌, route id 입력받기
     * @param routeId 여행지 리스트가 궁금한 경로의 id
     * @return 특정 경로의 여행지 리스트
     */
    @Override
    @GetMapping("/{routeId}/spots")
    public ResponseEntity<List<SpotResponseDTO>> getSpots(@PathVariable("routeId") Long routeId) {
        return ResponseEntity.ok(routeService.getSpots(routeId));
    /*
    {
      spots: [스팟 이름, 경도 , 위도]
    }

     */
    }



    /**
     * 좋아요 추가/취소
     * @param integratedId 좋아요하고 싶은 경로 ic
     */
    @PatchMapping("/like/{routeId}")
    public ResponseEntity<Void> addOrRemoveLike(@PathVariable("routeId") Long integratedId) {
        routeService.addOrRemoveLike(integratedId);
        return ResponseEntity.ok().build();
    }


    /**
     * 북마크 추가/취소
     * @param integratedId 북마크하고 싶은 경로 id
     */
    @PatchMapping("/bookmark/{routeId}")
    public ResponseEntity<Void> addOrRemoveBookmark(@PathVariable("routeId") Long integratedId) {
        routeService.addOrRemoveBookmark(integratedId);
        return ResponseEntity.ok().build();
    }

    /**
     * 자신이 좋아요한 경로 리스트
     * @return [경로 id, 경로 이름, 해당되는 경로의 여행지 리스트] 리스트 리턴
     */
    @GetMapping("/like")
    public ResponseEntity<List<RouteDetailResponseDTO>> findLike() {
        return ResponseEntity.ok(routeService.findLike());
        /*
        route {
            routeId:
            name : string
            spots : [
            spot,spot,spot (순서 정리된채로)
            ]
        }
         */
    }


    /**
     * 자신이 북마크한 경로 리스트
     * @return [경로 id, 경로 이름, 해당되는 경로의 여행지 리스트] 리스트 리턴
     */
    @GetMapping("/bookmark")
    public ResponseEntity<List<RouteDetailResponseDTO>> findBookmark() {
        return ResponseEntity.ok(routeService.findBookmark());
        /*
        route {
            routeId:
            name : string
            spots : [
            spot,spot,spot (순서 정리된채로)
            ]
        }
         */
    }

    /**
     * 자신이 생성한 경로 리스트
     * @return [경로 id, 경로 이름, 해당되는 경로의 여행지 리스트] 리스트 리턴
     */
    @GetMapping
    public ResponseEntity<List<RouteDetailResponseDTO>> findMyRoute() {
        /*
        route {
            routeId:
            name : string
            spots : [
            spot,spot,spot (순서 정리된채로)
            ]
        }
         */
        // 경로 이름 때문에 여기서 통합 경로 리턴하는 건 안될듯
        return ResponseEntity.ok(routeService.findMyRoute());
    }


    /**
     * 경로 이름 수정
     * @param requestDto 경로 id, 새로운 경로 이름
     */
    @PatchMapping("/name")
    public ResponseEntity<Void> updateName(@RequestBody UpdateRouteNameRequestDTO requestDto) {
        routeService.updateName(requestDto);
        return ResponseEntity.ok().build();
    }
}
