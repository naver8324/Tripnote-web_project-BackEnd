package com.elice.tripnote.domain.route.service;

import com.elice.tripnote.domain.hashtag.entity.Hashtag;
import com.elice.tripnote.domain.hashtag.repository.HashtagRepository;
import com.elice.tripnote.domain.integratedroute.entity.IntegratedRoute;
import com.elice.tripnote.domain.integratedroute.repository.IntegratedRouteRepository;
import com.elice.tripnote.domain.integratedroute.status.IntegratedRouteStatus;
import com.elice.tripnote.domain.likebookmarkperiod.entity.LikeBookmarkPeriod;
import com.elice.tripnote.domain.likebookmarkperiod.repository.LikeBookPeriodRepository;
import com.elice.tripnote.domain.link.bookmark.repository.BookmarkRepository;
import com.elice.tripnote.domain.link.routespot.entity.RouteSpot;
import com.elice.tripnote.domain.link.routespot.repository.RouteSpotRepository;
import com.elice.tripnote.domain.link.uuidhashtag.entity.UUIDHashtag;
import com.elice.tripnote.domain.link.uuidhashtag.repository.UUIDHashtagRepository;
import com.elice.tripnote.domain.member.repository.MemberRepository;
import com.elice.tripnote.domain.post.exception.NoSuchRouteException;
import com.elice.tripnote.domain.post.exception.NoSuchUserException;
import com.elice.tripnote.domain.post.repository.PostRepository;
import com.elice.tripnote.domain.route.entity.*;
import com.elice.tripnote.domain.route.exception.AlgorithmNotFoundException;
import com.elice.tripnote.domain.route.repository.RouteRepository;
import com.elice.tripnote.domain.route.status.RouteStatus;
import com.elice.tripnote.domain.spot.entity.Spot;
import com.elice.tripnote.domain.spot.repository.SpotRepository;
import com.elice.tripnote.global.exception.NoSuchSpotException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RouteService {
    private final RouteRepository routeRepository;
    private final IntegratedRouteRepository integratedRouteRepository;

    private final UUIDHashtagRepository uuidHashtagRepository;
    private final LikeBookPeriodRepository likeBookPeriodRepository;
    private final RouteSpotRepository routeSpotRepository;

    private final HashtagRepository hashtagRepository;
    private final MemberRepository memberRepository;
    private final SpotRepository spotRepository;
    private final PostRepository postRepository;
    private final BookmarkRepository bookmarkRepository;

    @Transactional
    public Long save(SaveRequestDTO requestDto) {
        //여행지 id 리스트 기반으로 uuid 만들기
        String uuid = generateUUID(requestDto.getSpotIds());

        // 만들어진 uuid 이용해서 integrated_route 객체 생성
        IntegratedRoute integratedRoute = integratedRouteRepository.findByIntegratedRoutes(uuid)
                .orElseGet(() -> {
                    IntegratedRoute newRoute = IntegratedRoute.builder()
                            .integratedRoutes(uuid)
                            //TODO: 나중에 여행지 주소가 어떻게 전달되는지 확인 후, 수정하기
                            .region(IntegratedRouteStatus.MULTI_REGION)
                            .build();
                    return integratedRouteRepository.save(newRoute);
                });


        // 통합 경로 객체(IntegratedRoute) 이용해서 uuid_hashtag 객체 생성
        // 현재 db에서 integratedRoute와 연관된 해시태그 찾기(이미 저장돼있는 해시태그)
        List<Long> dbHashtagIds = uuidHashtagRepository.findHashtagIdsByIntegratedRouteId(integratedRoute.getId());

        // 저장되어 있지 않아 새롭게 추가해야하는 해시태그 추출
        List<Long> newHashtagIds = requestDto.getHashtagIds().stream()
                .filter(id -> !dbHashtagIds.contains(id))
                .collect(Collectors.toList());

        // 추가해야하는 해시태그 아이디들의 객체 찾기
        List<Hashtag> hashtags = newHashtagIds.stream()
                .map(hashtagRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());


        for (Hashtag hashtag : hashtags) {
            UUIDHashtag uuidHashtag = UUIDHashtag.builder()
                    .hashtag(hashtag)
                    .integratedRoute(integratedRoute)
                    .build();
            uuidHashtagRepository.save(uuidHashtag);
        }

        // 해당 통합경로 아이디 값을 가진 객체가 있는지 확인.
        if (!likeBookPeriodRepository.existsByIntegratedRoute(integratedRoute)) {
            LikeBookmarkPeriod likeBookmarkPeriod = LikeBookmarkPeriod.builder()
                    .integratedRoute(integratedRoute)
                    .likes(0)
                    .bookmark(0)
                    .build();
            likeBookPeriodRepository.save(likeBookmarkPeriod);
        }

        // route 객체 생성 -> 경로 저장
        Route route = Route.builder()
                .member(memberRepository.findById(requestDto.getMemberId())
                        .orElseThrow(() -> new NoSuchUserException()))
                .integratedRoute(integratedRoute)
                .routeStatus(RouteStatus.PUBLIC)
                .expense(requestDto.getExpense())
                .build();
        route = routeRepository.save(route);

        // route_spot 객체 생성
        List<Long> spotIds = requestDto.getSpotIds();
        for (int i = 0; i < spotIds.size(); i++) {
            Spot spot = spotRepository.findById(spotIds.get(i))
                    .orElseThrow(() -> new NoSuchSpotException());
            Long nextSpotId = (i + 1 < spotIds.size()) ? spotIds.get(i + 1) : null;
            RouteSpot routeSpot = RouteSpot.builder()
                    .route(route)
                    .spot(spot)
                    .sequence(i + 1)
                    .nextSpotId(nextSpotId)
                    .build();
            routeSpotRepository.save(routeSpot);
        }

        return route.getId();
    }


    //여행지 id 리스트를 매개변수로 전달
    private static String generateUUID(List<Long> ids) {
        try {
            // 식별자들을 문자열로 변환하고 결합
            StringBuilder combined = new StringBuilder();
            for (Long id : ids) {
                combined.append(id.toString());
            }

            // SHA-1 해시 생성
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] hash = md.digest(combined.toString().getBytes(StandardCharsets.UTF_8));

            // 해시의 앞 16 바이트를 사용해 UUID 생성
            long msb = 0;
            long lsb = 0;
            for (int i = 0; i < 8; i++) {
                msb = (msb << 8) | (hash[i] & 0xff);
            }
            for (int i = 8; i < 16; i++) {
                lsb = (lsb << 8) | (hash[i] & 0xff);
            }

            return new UUID(msb, lsb).toString();
        } catch (NoSuchAlgorithmException e) {
            throw new AlgorithmNotFoundException();
        }
    }

    @Transactional
    public Long setRouteToPrivate(Long routeId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new NoSuchRouteException());
        route.setRouteStatus(RouteStatus.PRIVATE);
        route = routeRepository.save(route);
        return route.getId();
    }

    @Transactional
    public Long setRouteToPublic(Long routeId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new NoSuchRouteException());
        route.setRouteStatus(RouteStatus.PUBLIC);
        route = routeRepository.save(route);
        return route.getId();
    }

    @Transactional
    public Long deleteRoute(Long routeId) {
        log.info("{}번 경로가 삭제됩니다.", routeId);
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new NoSuchRouteException());
        route.setRouteStatus(RouteStatus.DELETE);
        route = routeRepository.save(route);
        return route.getId();
    }

    public List<Long> getRegion(IntegratedRouteStatus region, List<Long> hashtags) {
        // 통합 경로 중, 해당 지역을 지나는 통합 경로 필터링하고
        // 통합 경로의 지역이 region인 경로들 필터링

        /*
        1. 통합 경로에서 region으로 필터링하기
        2. 통합 경로 id 중, 해시태그_uuid_연결 테이블의 hashtag_id에 hashtags 값이 모두 있는 애들 필터링
        3. 이렇게 나온 통합 경로 id와 기간별_좋아요_북마크 join해서 기간별 좋아요 수를 기준으로 통합 경로 id를 정렬한다
        4. 이렇게 정렬한 것 중 상위 5개의 통합 경로 id를 리턴한다
         */

        /*
        SELECT ir.id AS integrated_route_id, SUM(plb.likes) AS total_likes
        FROM integrated_route ir
        left JOIN uuid_hashtag uh ON ir.id = uh.integrated_route_id
        JOIN like_bookmark_period lbp ON ir.id = lbp.integrated_route_id
        WHERE ir.region = :region
          AND uh.hashtag_id IN :hashtags  -- 제시된 해시태그 id 안에 속하는 row만 남김
        GROUP BY ir.id
        HAVING COUNT(DISTINCT uh.hashtag_id) = :hashtags_size  -- 그룹별로 묶었을 때, 해당 그룹의 해시태그 id 개수가 hashtags의 개수와 같으면 모든 hashtag가 포함된거?
            and lbp.started_at = max(lbp.started_at) -- 그룹별로 묶었을 때 started_at 값이 가장 큰 row만 남게
        ORDER BY lbp.likes DESC
        LIMIT 5;
         */
        List<IntegratedRouteDTO> dtos=integratedRouteRepository.findTopIntegratedRoutesByRegionAndHashtags(region, hashtags);

        // 그 중에서 최근 좋아요(like bookmark period 이용) 많은 수 top 5 경로 id를 리턴
        List<Long> integratedIds = new ArrayList<>();
        for(IntegratedRouteDTO dto : dtos){
            integratedIds.add(dto.getIntegratedRouteId());
        }
        return integratedIds;
    }

    public List<SpotResponseDTO> getSpots(Long integratedRouteId) {
        return spotRepository.findByRouteIds(integratedRouteId);
    }


    public LikeBookmarkResponseDTO getLikeBookmark(Long integratedRouteId) {
        // 해당 통합 경로 id를 가지고 있는 모든 route들의 좋아요 수 합치기
        // 만약 게시물이 있으면 더하고, 없으면 패스

            /*
            select sum(post.likes) from post
            join route
            on post.route_id = route.id and route.integrated_route_id = :integratedRouteId
             */
        int like = postRepository.getLikeCount(integratedRouteId);

            /*
            select count(*) from bookmark b
            join route r on r.id = b.route_id
            where r.id = :integratedRouteId
             */
        int bookmark = bookmarkRepository.getBookmarkCount(integratedRouteId);

        return LikeBookmarkResponseDTO.builder()
                .likes(like)
                .bookmark(bookmark)
                .build();
    }

    public List<Long> getRoutesThroughSpot(List<Long> hashtags, List<Long> spots) {

        /*
        1. 통합 경로의 여행지에 spots가 모두 포함되는거만 필터링
        2. 통합 경로 id 중, 해시태그_uuid_연결 테이블의 hashtag_id에 hashtags 값이 모두 있는 애들 필터링
        3. 이렇게 나온 통합 경로 id와 기간별_좋아요_북마크 join해서 기간별 좋아요 수를 기준으로 통합 경로 id를 정렬한다
        4. 이렇게 정렬한 것 중 상위 5개의 통합 경로 id를 리턴한다
         */

        // 먼저 경로 중, spots가 모두 포함되는 거 찾기
        // 그리고 같은 통합 경로 id를 가진 애들끼리 묶어서 통합 경로 id 리턴 (ids로)

        log.info("여행지 id: {}", spots);
        /*
        select distinct r.integrated_route_id
        from route r
        join route_spot rs on rs.route_id = r.id
        where rs.spot_id in :spots  -- spot_id가 spots 중에 하나일 때만 통과
        group by r.id, r.integrated_route_id    -- 같은 통합 경로 id 그룹 안에 각 경로 id끼리 그룹
        having count(distinct rs.spot_id) = :spots.size()   -- route id 기준으로 묶었을 때, 겹치지 않는 spot_id가 spots.size개인 row만
                                                               (spot_id가 spots 중에 하나일 때만 통과이므로 spot_id가 spots의 크기라면 모든 spots가 있음)
         */
        List<Long> integratedIds = routeRepository.findIntegratedRouteIdsBySpots(spots);
        log.info("통합 경로 id들: {}", integratedIds);

        /*
        SELECT ir.id AS integrated_route_id, SUM(plb.likes) AS total_likes
        FROM integrated_route ir
        left JOIN uuid_hashtag uh ON ir.id = uh.integrated_route_id
        JOIN like_bookmark_period lbp ON ir.id = lbp.integrated_route_id
        WHERE ir.id in :ids
          AND uh.hashtag_id IN :hashtags  -- 제시된 해시태그 id 안에 속하는 row만 남김
        GROUP BY ir.id
        HAVING COUNT(DISTINCT uh.hashtag_id) = :hashtags.size  -- 그룹별로 묶었을 때, 해당 그룹의 해시태그 id 개수가 hashtags의 개수와 같으면 모든 hashtag가 포함된거?
            and count(distinct ir.id) = :ids.size
            and lbp.started_at = max(lbp.started_at) -- 그룹별로 묶었을 때 started_at 값이 가장 큰 row만 남게
        ORDER BY lbp.likes DESC
        LIMIT 5;
         */
        log.info("해시태그 id: {}", hashtags);
        List<IntegratedRouteDTO> dtos=integratedRouteRepository.findIntegratedRouteFilterByHashtags(integratedIds, hashtags);

        // 그 중에서 최근 좋아요(like bookmark period 이용) 많은 수 top 5 경로 id를 리턴
        List<Long> result = new ArrayList<>();
        for(IntegratedRouteDTO dto : dtos){
            result.add(dto.getIntegratedRouteId());
        }
        log.info("통합 경로 id들(최종 결과): {}", result);
        return result;


        //gpt가 합친거
        /*
        SELECT ir.id AS integrated_route_id, SUM(plb.likes) AS total_likes
        FROM integrated_route ir
        LEFT JOIN uuid_hashtag uh ON ir.id = uh.integrated_route_id
        JOIN like_bookmark_period lbp ON ir.id = lbp.integrated_route_id
        JOIN (
            SELECT r.id
            FROM route r
            JOIN route_spot rs ON rs.route_id = r.id
            WHERE rs.spot_id IN :spots
            GROUP BY r.id, r.integrated_route_id
            HAVING COUNT(DISTINCT rs.spot_id) = :spots.size()
        ) AS routes ON routes.id = ir.id
        WHERE ir.id IN :ids
          AND uh.hashtag_id IN :hashtags
        GROUP BY ir.id
        HAVING COUNT(DISTINCT uh.hashtag_id) = :hashtags.size
           AND COUNT(DISTINCT ir.id) = :ids.size
           AND lbp.started_at = MAX(lbp.started_at)
        ORDER BY lbp.likes DESC
        LIMIT 5;
         */

    }


}
