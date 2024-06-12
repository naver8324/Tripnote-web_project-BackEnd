package com.elice.tripnote.domain.route.service;

import com.elice.tripnote.domain.hashtag.entity.Hashtag;
import com.elice.tripnote.domain.hashtag.repository.HashtagRepository;
import com.elice.tripnote.domain.integratedroute.entity.IntegratedRoute;
import com.elice.tripnote.domain.integratedroute.repository.IntegratedRouteRepository;
import com.elice.tripnote.domain.likebookmarkperiod.entity.LikeBookmarkPeriod;
import com.elice.tripnote.domain.likebookmarkperiod.repository.LikeBookPeriodRepository;
import com.elice.tripnote.domain.link.bookmark.entity.Bookmark;
import com.elice.tripnote.domain.link.bookmark.repository.BookmarkRepository;
import com.elice.tripnote.domain.link.likePost.entity.LikePost;
import com.elice.tripnote.domain.link.likePost.repository.LikePostRepository;
import com.elice.tripnote.domain.link.routespot.entity.RouteSpot;
import com.elice.tripnote.domain.link.routespot.repository.RouteSpotRepository;
import com.elice.tripnote.domain.link.uuidhashtag.entity.UUIDHashtag;
import com.elice.tripnote.domain.link.uuidhashtag.repository.UUIDHashtagRepository;
import com.elice.tripnote.domain.member.entity.Member;
import com.elice.tripnote.domain.member.repository.MemberRepository;
import com.elice.tripnote.domain.post.entity.Post;
import com.elice.tripnote.domain.post.repository.PostRepository;
import com.elice.tripnote.domain.route.entity.*;
import com.elice.tripnote.domain.route.repository.RouteRepository;
import com.elice.tripnote.domain.route.status.RouteStatus;
import com.elice.tripnote.domain.spot.constant.Region;
import com.elice.tripnote.domain.spot.entity.Spot;
import com.elice.tripnote.domain.spot.repository.SpotRepository;
import com.elice.tripnote.global.entity.PageRequestDTO;
import com.elice.tripnote.global.exception.CustomException;
import com.elice.tripnote.global.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;
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
    private final BookmarkRepository bookmarkRepository;
    private final LikePostRepository likePostRepository;
    private final PostRepository postRepository;

    @Transactional
    public Long save(SaveRequestDTO requestDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("유저 이메일: {}", email);
        //여행지 id 리스트 기반으로 uuid 만들기
        String uuid = generateUUID(requestDto.getSpotIds());

        Region region = findRegionOfSpots(requestDto.getSpotIds());

        // 만들어진 uuid 이용해서 integrated_route 객체 생성
        IntegratedRoute integratedRoute = integratedRouteRepository.findByIntegratedRoutes(uuid)
                .orElseGet(() -> {
                    IntegratedRoute newRoute = IntegratedRoute.builder()
                            .integratedRoutes(uuid)
                            .region(region)
                            .routeStatus(RouteStatus.PUBLIC)
                            .build();
                    return integratedRouteRepository.save(newRoute);
                });

        if (region != Region.MIXED_REGION) {
            Long regionHashtagId = (long) region.getIndex() + 1;
            log.info("지역 해시태그 아이디: {}", regionHashtagId);
            if (routeRepository.findHashtagIdIdCity(regionHashtagId)) requestDto.getHashtagIds().add(regionHashtagId);
        }

//        if (requestDto.getHashtagIds() != null) {
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
//        }


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
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> {
            throw new CustomException(ErrorCode.NO_MEMBER);
        });
        Route route = Route.builder()
                .member(member)
                .integratedRoute(integratedRoute)
                .routeStatus(RouteStatus.PUBLIC)
                // expense 값이 안들어왔다면 0으로 초기화
                .expense(requestDto.getExpense() != 0 ? requestDto.getExpense() : 0)
                .name(requestDto.getName())
                .build();
        route = routeRepository.save(route);

        // route_spot 객체 생성
        List<Long> spotIds = requestDto.getSpotIds();
        for (int i = 0; i < spotIds.size(); i++) {
            Spot spot = spotRepository.findById(spotIds.get(i))
                    .orElseThrow(() -> {
                        throw new CustomException(ErrorCode.NO_SPOT);
                    });
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

    private Region findRegionOfSpots(List<Long> spotIds) {
        Region region = spotRepository.getRegionByspotId(spotIds.get(0)).getRegion();
        for (Long id : spotIds) {
            if (region != spotRepository.getRegionByspotId(id).getRegion()) return Region.MIXED_REGION;
        }
        return region;
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
            throw new CustomException(ErrorCode.NOT_FOUND_ALGORITHM);
        }
    }

//    @Transactional
//    public Long setRouteToStatus(Long routeId) {
//        Member member = getMemberFromJwt();
//
//        Route route = routeRepository.findById(routeId)
//                .orElseThrow(() -> {
//                    throw new CustomException(ErrorCode.NO_ROUTE);
//                });
//
//        // 해당 경로가 자신의 것이 맞는지 확인
//        if (member.getId() != route.getMember().getId())
//            throw new CustomException(ErrorCode.UNAUTHORIZED_UPDATE_STATUS);
//        if (route.getRouteStatus() == RouteStatus.PUBLIC) route.updateStatus(RouteStatus.PRIVATE);
//        else if (route.getRouteStatus() == RouteStatus.PRIVATE) route.updateStatus(RouteStatus.PUBLIC);
//        else {
//            log.warn("삭제된 경로입니다. 경로 번호: {}", routeId);
//            throw new CustomException(ErrorCode.NO_ROUTE);
//        }
//
//        return routeRepository.save(route).getId();
//    }

    @Transactional
    public Long deleteRoute(Long routeId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> {
            throw new CustomException(ErrorCode.NO_MEMBER);
        });

        log.info("{}번 경로가 삭제됩니다.", routeId);
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> {
                    throw new CustomException(ErrorCode.NO_ROUTE);
                });

        // 해당 경로가 자신의 것이 맞는지 확인
        if (member.getId() != route.getMember().getId()) throw new CustomException(ErrorCode.UNAUTHORIZED_DELETE);

        route.updateStatus(RouteStatus.DELETE);
        route = routeRepository.save(route);

        integratedRouteRepository.deleteIntegratedRoute(route.getIntegratedRoute().getId());
        return route.getId();
    }

    public RecommendedRouteResponseDTO getRouteInfo(Long routeId) {
        Member member = getMemberFromJwt();
        // routeid를 이용해서 통합 경로 id 가져오기
        Route route = routeRepository.findById(routeId).orElseThrow(() -> {
            throw new CustomException(ErrorCode.NO_ROUTE);
        });
        Long integratedRouteId = route.getIntegratedRoute().getId();
        return RecommendedRouteResponseDTO.builder()
                .integratedRouteId(integratedRouteId)
                .postId(
                        postRepository.findByRouteId(routeId)
                                .map(post -> post.getId())
                                .orElse(null)
                )
                .spots(spotRepository.findSpotsByIntegratedRouteIdInOrder(integratedRouteId)) // 해당 route에 맞는 spots구하기
                .likes(routeRepository.getIntegratedRouteLikeCounts(integratedRouteId)) // 해당 경로의 좋아요 수
                .likedAt(routeRepository.existsByMemberIdAndIntegratedRouteId(member.getId(), integratedRouteId, true)) // 자신이 이 경로에 좋아요를 눌렀는지
                .markedAt(routeRepository.existsByMemberIdAndIntegratedRouteId(member.getId(), integratedRouteId, false)) // 자신이 이 경로에 북마크를 눌렀는지
                .build();
    }

    public List<RecommendedRouteResponseDTO> getRegionMember(Region region) {
        Member member = getMemberFromJwt();
        return getRegion(region, true, member);
    }

    public List<RecommendedRouteResponseDTO> getRegionGuest(Region region) {
        return getRegion(region, false, null);
    }

    private List<RecommendedRouteResponseDTO> getRegion(Region region, boolean isMember, Member member) {
        List<Long> integratedIds = integratedRouteRepository.findTopIntegratedRoutesByRegionAndHashtags(region);
        log.info("리턴되는 통합 경로 id: {}", integratedIds);
        return getRecommendRoutesByIntegratedRoutes(integratedIds, isMember, member);
    }

    private List<RecommendedRouteResponseDTO> getRecommendRoutesByIntegratedRoutes(List<Long> integratedRouteIds, boolean isMember, Member member) {

        // 자신은 해당 route에 좋아요 눌렀는지, 북마크 눌렀는지 여부
//        List<RecommendedRouteResponseDTO> recommendedRouteResponseDTOS = new ArrayList<>();
//        for (Long irId : integratedRouteIds) {
//            recommendedRouteResponseDTOS.add(RecommendedRouteResponseDTO.builder()
//                    .integratedRouteId(irId)
//                    .postId(routeRepository.findPostIdByIntegratedRouteId(irId))
//                    .spots(spotRepository.findSpotsByIntegratedRouteIdInOrder(irId)) // 해당 route에 맞는 spots구하기
//                    .likes(routeRepository.getIntegratedRouteLikeCounts(irId)) // 해당 경로의 좋아요 수
//                    .likedAt(isMember ? likePostRepository.existsByMemberIdAndIntegratedRouteId(member.getId(), irId) : false) // 자신이 이 경로에 좋아요를 눌렀는지
//                    .markedAt(isMember ? bookmarkRepository.existsByMemberIdAndIntegratedRouteId(member.getId(), irId) : false) // 자신이 이 경로에 북마크를 눌렀는지
//                    .build());
//        }
//        return recommendedRouteResponseDTOS;
        return routeRepository.getRecommendedRoutes(integratedRouteIds, isMember ? member.getId() : null, isMember);

        /*
        integrated route id를 이용해서 가장 좋아요 많은 post id 구하기
        integrated route id를 이용해서 경로의 spots 구하기
        integrated route id 이용해서 좋아요 개수 구하기
        해당 유저가 좋아요, 북마크 눌렀는지 알아내기

        select p.id
        from post p
        join like_post lp on lp.post_id=p.id
        join route r on r.id=p.route_id
        join integrated_route ir on ir.id=r.integrated_route_id
        where ir.id=(:integratedRouteId)
        group by p.id
        order by count(lp.member_id) desc
        limit 1;



        JPQLQuery<Long> subquery = JPAExpressions
                        .select(route.id.min())
                        .from(route)
                        .where(route.integratedRoute.id.eq(integratedRouteId));


        select count(lp.id)
        from like_post lp
        where lp.route_id=(:subquery)

        select count(lp.id)
        from like_post lp
        where lp.member_id=(:memberId) and lp.route_id=(:subquery)

        select count(lp.id)
        from bookmark b
        where b.member_id=(:memberId) and b.route_id=(:subquery)


        //아래 쿼리는 List로 들어가게
        select s
        from spot s
        join route_spot rs on rs.spot_id=s.id
        join route r on r.id=rs.route_id
        where r.id=(:subquery)

         */


    }


    // 게시물에서 경로 표현할 때 사용?
    public List<SpotResponseDTO> getSpots(Long routeId) {
        return spotRepository.findByRouteIds(routeId);
    }

    public List<RecommendedRouteResponseDTO> getRoutesThroughSpotMember(List<Long> spots) {
        Member member = getMemberFromJwt();
        return getRoutesThroughSpot(spots, true, member);
    }

    public List<RecommendedRouteResponseDTO> getRoutesThroughSpotGuest(List<Long> spots) {
        return getRoutesThroughSpot(spots, false, null);
    }

    private List<RecommendedRouteResponseDTO> getRoutesThroughSpot(List<Long> spots, boolean isMember, Member member) {

        /*
        1. 통합 경로의 여행지에 spots가 모두 포함되는거만 필터링
        2. 통합 경로 id 중, 해시태그_uuid_연결 테이블의 hashtag_id에 hashtags 값이 모두 있는 애들 필터링
        3. 이렇게 나온 통합 경로 id와 기간별_좋아요_북마크 join해서 기간별 좋아요 수를 기준으로 통합 경로 id를 정렬한다
        4. 이렇게 정렬한 것 중 상위 5개의 통합 경로 id를 리턴한다
         */

        // 먼저 경로 중, spots가 모두 포함되는 거 찾기
        // 그리고 같은 통합 경로 id를 가진 애들끼리 묶어서 통합 경로 id 리턴 (ids로)

        // 해당하는 여행지들이 모두 있는 통합 경로 id 리턴
        //TODO: findIntegratedRouteIdsBySpots, findIntegratedRoute 하나로 합치기
//        List<Long> integratedIds = routeRepository.findIntegratedRouteIdsBySpots(spots);
//        log.info("통합 경로 id들: {}", integratedIds);
//        integratedIds = integratedRouteRepository.findIntegratedRoute(integratedIds);
//        log.info("최종적인 통합 경로 id들: {}", integratedIds)
//       ;
        List<Long> integratedIds = routeRepository.findIntegratedRouteIdsBySpotsAndLikes(spots);
        log.info("리턴되는 통합 경로 id: {}", integratedIds);

        return getRecommendRoutesByIntegratedRoutes(integratedIds, isMember, member);
    }

    @Transactional
    public void addOrRemoveLike(Long integratedId) {
        Member member = getMemberFromJwt();

        //TODO: 가장 최신의 객체 가져오기(스케줄러 필요)
        LikeBookmarkPeriod likeBookmarkPeriod = likeBookPeriodRepository.findByIntegratedRouteId(integratedId)
                .orElseThrow(() -> {
                    throw new CustomException(ErrorCode.NO_LIKE_BOOKMARK_PERIOD);
                });

        // 해당 통합 경로의 route 중에서 가장 작은 id값을 가진 경로 확인하기
        // 해당 경로랑 member 조인해서 검색 후, 없다면 아래 로직 있다면 없애기
        if (routeRepository.existsByMemberIdAndIntegratedRouteId(member.getId(), integratedId, true)) {
            log.info("통합경로 {}번 경로의 좋아요를 취소하겠습니다.", integratedId);
            likeBookmarkPeriod.updateLike(likeBookmarkPeriod.getLikes() - 1);
            routeRepository.deleteByMemberIdAndIntegratedRouteId(member.getId(), integratedId, true);
            return;
        }

        // 없는 경우
        // 해당 통합 경로의 route 중에서 가장 작은 id값을 가진 route에다가 좋아요 추가하기
        log.info("통합경로 {}번 경로에 좋아요를 추가하겠습니다.", integratedId);
        Route route = routeRepository.getMinRouteByIntegratedId(integratedId);

        LikePost likePost = LikePost.builder()
                .member(member)
                .route(route)
                .likedAt(LocalDateTime.now())
                .build();
        likePostRepository.save(likePost);

        likeBookmarkPeriod.updateLike(likeBookmarkPeriod.getLikes() + 1);
        likeBookPeriodRepository.save(likeBookmarkPeriod);

    }

    @Transactional
    public void addOrRemoveBookmark(Long integratedId) {
        Member member = getMemberFromJwt();

        //TODO: 가장 최신의 객체 가져오기(스케줄러 필요)
        LikeBookmarkPeriod likeBookmarkPeriod = likeBookPeriodRepository.findByIntegratedRouteId(integratedId)
                .orElseThrow(() -> {
                    throw new CustomException(ErrorCode.NO_LIKE_BOOKMARK_PERIOD);
                });

        //member랑 route 조인해서 검색 후, 없다면 아래 로직 있다면 없애기
        if (routeRepository.existsByMemberIdAndIntegratedRouteId(member.getId(), integratedId, false)) {
            log.info("통합경로 {}번 경로의 북마크를 취소하겠습니다.", integratedId);
            likeBookmarkPeriod.updateBookmark(likeBookmarkPeriod.getBookmark() - 1);
            routeRepository.deleteByMemberIdAndIntegratedRouteId(member.getId(), integratedId, false);
            return;
        }

        // 없는 경우
        log.info("통합경로 {}번 경로에 북마크를 추가하겠습니다.", integratedId);
        Route route = routeRepository.getMinRouteByIntegratedId(integratedId);

        Bookmark bookmark = Bookmark.builder()
                .member(member)
                .route(route)
                .markedAt(LocalDateTime.now())
                .build();
        bookmarkRepository.save(bookmark);

        likeBookmarkPeriod.updateBookmark(likeBookmarkPeriod.getBookmark() + 1);
        likeBookPeriodRepository.save(likeBookmarkPeriod);
    }

    @Transactional
    public void updateName(UpdateRouteNameRequestDTO dto) {
        Member member = getMemberFromJwt();
        Route route = routeRepository.findByIdAndMemberId(dto.getRouteId(), member.getId());
        if (route == null) throw new CustomException(ErrorCode.UNAUTHORIZED_UPDATE_NAME);
        route.updateRouteName(dto.getName());
        routeRepository.save(route);
    }

    public Page<RouteDetailResponseDTO> findBookmark(PageRequestDTO pageRequestDTO) {
        Member member = getMemberFromJwt();
        return routeRepository.findRouteDetailsByMemberId(member.getId(), pageRequestDTO, true);
    }


    public Page<RouteDetailResponseDTO> findMyRoute(PageRequestDTO pageRequestDTO) {
        Member member = getMemberFromJwt();
        return routeRepository.findRouteDetailsByMemberId(member.getId(), pageRequestDTO, false);
    }

    private Member getMemberFromJwt() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return memberRepository.findByEmail(email).orElseThrow(() -> {
            throw new CustomException(ErrorCode.NO_MEMBER);
        });
    }


}
