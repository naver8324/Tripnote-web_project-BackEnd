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
import com.elice.tripnote.domain.route.entity.*;
import com.elice.tripnote.domain.route.repository.RouteRepository;
import com.elice.tripnote.domain.route.status.RouteStatus;
import com.elice.tripnote.domain.spot.constant.Region;
import com.elice.tripnote.domain.spot.entity.Spot;
import com.elice.tripnote.domain.spot.repository.SpotRepository;
import com.elice.tripnote.global.exception.CustomException;
import com.elice.tripnote.global.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
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
    private final BookmarkRepository bookmarkRepository;
    private final LikePostRepository likePostRepository;

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
                            .build();
                    return integratedRouteRepository.save(newRoute);
                });


        if (requestDto.getHashtagIds() != null) {
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

    @Transactional
    public Long setRouteToStatus(Long routeId) {
        Member member = getMemberFromJwt();

        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> {
                    throw new CustomException(ErrorCode.NO_ROUTE);
                });

        // 해당 경로가 자신의 것이 맞는지 확인
        if (member.getId() != route.getMember().getId())
            throw new CustomException(ErrorCode.UNAUTHORIZED_UPDATE_STATUS);
        if(route.getRouteStatus() == RouteStatus.PUBLIC) route.updateStatus(RouteStatus.PRIVATE);
        else if(route.getRouteStatus() == RouteStatus.PRIVATE) route.updateStatus(RouteStatus.PUBLIC);
        else {
            log.warn("삭제된 경로입니다. 경로 번호: {}", routeId);
            throw new CustomException(ErrorCode.NO_ROUTE);
        }

        return routeRepository.save(route).getId();
    }

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
        return route.getId();
    }

    public List<RecommendedRouteResponseDTO> getRegionMember(Region region){
        Member member = getMemberFromJwt();
        return getRegion(region,true, member);
    }

    public List<RecommendedRouteResponseDTO> getRegionGuest(Region region){
        return getRegion(region,false, null);
    }

    private List<RecommendedRouteResponseDTO> getRegion(Region region, boolean isMember, Member member) {

        List<Long> integratedIds = integratedRouteRepository.findTopIntegratedRoutesByRegionAndHashtags(region);

        // 자신은 해당 route에 좋아요 눌렀는지, 북마크 눌렀는지 여부
        List<RecommendedRouteResponseDTO> recommendedRouteResponseDTOS = new ArrayList<>();

        for (Long irId : integratedIds) {
            recommendedRouteResponseDTOS.add(RecommendedRouteResponseDTO.builder()
                    .routeId(irId)
                    .spots(spotRepository.findSpotsByIntegratedRouteIdInOrder(irId)) // 해당 route에 맞는 spots구하기
                    .likes(routeRepository.getIntegratedRouteLikeCounts(irId)) // 해당 경로의 좋아요 수
                    .likedAt(isMember ? likePostRepository.existsByMemberIdAndIntegratedRouteId(member.getId(), irId) : false) // 자신이 이 경로에 좋아요를 눌렀는지
                    .markedAt(isMember ? bookmarkRepository.existsByMemberIdAndIntegratedRouteId(member.getId(), irId) : false) // 자신이 이 경로에 북마크를 눌렀는지
                    .build());
        }

        /*
        select count(id)
        from like_post lp
        where lp.member_id=:memberId
            and lp.route_id=:routeId
         */


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
        return recommendedRouteResponseDTOS;
    }



    // 게시물에서 경로 표현할 때 사용?
    public List<SpotResponseDTO> getSpots(Long routeId) {
        return spotRepository.findByRouteIds(routeId);
    }

    public List<RecommendedRouteResponseDTO> getRoutesThroughSpotMember(List<Long> spots){
        Member member = getMemberFromJwt();
        return getRoutesThroughSpot(spots,true, member);
    }

    public List<RecommendedRouteResponseDTO> getRoutesThroughSpotGuest(List<Long> spots){
        return getRoutesThroughSpot(spots,false, null);
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
        List<Long> integratedIds = routeRepository.findIntegratedRouteIdsBySpots(spots);
        log.info("통합 경로 id들: {}", integratedIds);
        integratedIds = integratedRouteRepository.findIntegratedRoute(integratedIds);

        // 자신은 해당 route에 좋아요 눌렀는지, 북마크 눌렀는지 여부
        List<RecommendedRouteResponseDTO> recommendedRouteResponseDTOS = new ArrayList<>();
        for (Long irId : integratedIds) {
            log.info("현재 통합 경로 id -> {}", irId);
            recommendedRouteResponseDTOS.add(RecommendedRouteResponseDTO.builder()
                    .routeId(irId)
                    .spots(spotRepository.findSpotsByIntegratedRouteIdInOrder(irId)) // 해당 route에 맞는 spots구하기
                    .likes(routeRepository.getIntegratedRouteLikeCounts(irId)) // 해당 경로의 좋아요 수
                    .likedAt(isMember ? likePostRepository.existsByMemberIdAndIntegratedRouteId(member.getId(), irId) : false) // 자신이 이 경로에 좋아요를 눌렀는지
                    .markedAt(isMember ? bookmarkRepository.existsByMemberIdAndIntegratedRouteId(member.getId(), irId) : false) // 자신이 이 경로에 북마크를 눌렀는지
                    .build());
        }
        return recommendedRouteResponseDTOS;

    }

    @Transactional
    public void addOrRemoveLike(Long integratedId) {
        Member member = getMemberFromJwt();

        //TODO: 가장 최신의 객체 가져오기
        LikeBookmarkPeriod likeBookmarkPeriod = likeBookPeriodRepository.findByIntegratedRouteId(integratedId)
                .orElseThrow(() -> {
                    throw new CustomException(ErrorCode.NO_LIKE_BOOKMARK_PERIOD);
                });

        // 해당 통합 경로의 route 중에서 가장 작은 id값을 가진 경로 확인하기
        // 해당 경로랑 member 조인해서 검색 후, 없다면 아래 로직 있다면 없애기
        if (likePostRepository.existsByMemberIdAndIntegratedRouteId(member.getId(), integratedId)) {
            likeBookmarkPeriod.updateLike(likeBookmarkPeriod.getLikes() - 1);
            likePostRepository.deleteByMemberIdAndIntegratedRouteId(member.getId(), integratedId);
            return;
        }

        // 없는 경우
        // 해당 통합 경로의 route 중에서 가장 작은 id값을 가진 route에다가 좋아요 추가하기
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

        //TODO: 가장 최신의 객체 가져오기
        LikeBookmarkPeriod likeBookmarkPeriod = likeBookPeriodRepository.findByIntegratedRouteId(integratedId)
                .orElseThrow(() -> {
                    throw new CustomException(ErrorCode.NO_LIKE_BOOKMARK_PERIOD);
                });

        //member랑 route 조인해서 검색 후, 없다면 아래 로직 있다면 없애기
        if (bookmarkRepository.existsByMemberIdAndIntegratedRouteId(member.getId(), integratedId)) {
            likeBookmarkPeriod.updateLike(likeBookmarkPeriod.getBookmark() - 1);
            bookmarkRepository.deleteByMemberIdAndIntegratedRouteId(member.getId(), integratedId);
            return;
        }

        // 없는 경우
        //Route route = routeRepository.findById(routeId).orElseThrow(() -> {
        //    throw new CustomException(ErrorCode.NO_ROUTE);
        //});
        Route route = routeRepository.getMinRouteByIntegratedId(integratedId);

        Bookmark bookmark = Bookmark.builder()
                .member(member)
                .route(route)
                .markedAt(LocalDateTime.now())
                .build();
        bookmarkRepository.save(bookmark);

        likeBookmarkPeriod.updateLike(likeBookmarkPeriod.getBookmark() + 1);
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

//    public List<RouteDetailResponseDTO> findLike() {
//        Member member = getMemberFromJwt();
//        /*
//        select r.id, r.name
//        from Route r
//        join like_post lp on lp.route_id=r.id
//        where member_id=:memberId
//         */
//        List<RouteIdNameResponseDTO> routeIdNameDTOS = routeRepository.findLikedRoutesByMemberId(member.getId());
//
//        List<RouteDetailResponseDTO> routeDetailResponseDTOS = new ArrayList<>();
//        for (RouteIdNameResponseDTO dto : routeIdNameDTOS) {
//            routeDetailResponseDTOS.add(RouteDetailResponseDTO.builder()
//                    .routeId(dto.getRouteId())
//                    .name(dto.getName())
//                    .spots(spotRepository.findSpotsByRouteIdInOrder(dto.getRouteId()))
//                    /*
//                    select *
//                    from spot s
//                    join route_spot rs on rs.spot_id=s.id
//                    where rs.route_id=:routeId
//                    order by rs.sequence asc
//                     */
//                    .build());
//        }
//        return routeDetailResponseDTOS;
//    }

    public Page<RouteDetailResponseDTO> findBookmark(Pageable pageable) {
        Member member = getMemberFromJwt();
        /*
        select r.id, r.name
        from Route r
        join bookmark b on b.route_id=r.id
        where member_id=:memberId
         */
        Page<RouteIdNameResponseDTO> routeIdNameDTOS = routeRepository.findMarkedRoutesByMemberId(member.getId(), pageable);

        List<RouteDetailResponseDTO> routeDetailResponseDTOS = routeIdNameDTOS.getContent().stream()
                .map(dto -> RouteDetailResponseDTO.builder()
                        .routeId(dto.getRouteId())
                        .name(dto.getName())
                        .spots(spotRepository.findSpotsByRouteIdInOrder(dto.getRouteId()))
                        .build())
                .collect(Collectors.toList());

        return new PageImpl<>(routeDetailResponseDTOS, pageable, routeIdNameDTOS.getTotalElements());
    }


    public Page<RouteDetailResponseDTO> findMyRoute(Pageable pageable) {
        Member member = getMemberFromJwt();
        /*
        select r.id, r.name
        from Route r
        where member_id=:memberId
         */
        Page<RouteIdNameResponseDTO> routeIdNameDTOS = routeRepository.findRoutesByMemberId(member.getId(), pageable);

        List<RouteDetailResponseDTO> routeDetailResponseDTOS = new ArrayList<>();
        for (RouteIdNameResponseDTO dto : routeIdNameDTOS) {
            routeDetailResponseDTOS.add(RouteDetailResponseDTO.builder()
                    .routeId(dto.getRouteId())
                    .name(dto.getName())
                    .spots(spotRepository.findSpotsByRouteIdInOrder(dto.getRouteId()))
                    .build());
        }
        return new PageImpl<>(routeDetailResponseDTOS, pageable, routeIdNameDTOS.getTotalElements());
    }

    private Member getMemberFromJwt() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return memberRepository.findByEmail(email).orElseThrow(() -> {
            throw new CustomException(ErrorCode.NO_MEMBER);
        });
    }


}
