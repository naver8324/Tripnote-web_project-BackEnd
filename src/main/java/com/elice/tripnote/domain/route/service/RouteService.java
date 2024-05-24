package com.elice.tripnote.domain.route.service;

import com.elice.tripnote.domain.hashtag.entity.Hashtag;
import com.elice.tripnote.domain.hashtag.repository.HashtagRepository;
import com.elice.tripnote.domain.integratedroute.entity.IntegratedRoute;
import com.elice.tripnote.domain.integratedroute.repository.IntegratedRouteRepository;
import com.elice.tripnote.domain.integratedroute.status.IntegratedRouteStatus;
import com.elice.tripnote.domain.likebookmarkperiod.repository.LikeBookPeriodRepository;
import com.elice.tripnote.domain.route.dto.SaveRequestDto;
import com.elice.tripnote.domain.route.repository.RouteRepository;
import com.elice.tripnote.domain.link.routespot.repository.RouteSpotRepository;
import com.elice.tripnote.domain.link.uuidhashtag.entity.UUIDHashtag;
import com.elice.tripnote.domain.link.uuidhashtag.repository.UUIDHashtagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    @Transactional
    public Long save(SaveRequestDto requestDto) {
        //여행지 id 리스트 기반으로 uuid 만들기
        //통합 경로 객체 저장하기 -> 지역내여부 컬럼...? 그냥 지역 이름 저장하는건가?
        IntegratedRoute integratedRoute = getIntegratedRoute(requestDto.getSpotIds());
        saveUUIDHashtag(requestDto.getHashtagIds(), integratedRoute);

        //TODO: 위에서 구한 통합 경로 id를 이용해서 (먼저 동일한 통합 경로 id가 있는지 탐색, 없다면) 기간별 좋아요 북마크 객체 생성

        // 위에서 구한 통합 경로 id를 이용해서 경로 객체 저장
        // 유저 번호, 통합 경로 번호, 기본적으로 공개 상태
        // 비용은 여행지별 경비 리스트의 총합

        // 경로 여행지 연결 객체 생성하기
        // 위에서 구한 경로 id를 이용

        //경로 id 리턴
        return  null;
    }

    private IntegratedRoute getIntegratedRoute(List<Long> spotIds){
        String uuid = generateUUID(spotIds);

        IntegratedRoute integratedRoute = integratedRouteRepository.findByIntegratedRoutes(uuid)
                .orElseGet(() -> {
                    IntegratedRoute newRoute = IntegratedRoute.builder()
                            .integratedRoutes(uuid)
                            //TODO: 나중에 여행지 주소가 어떻게 전달되는지 확인 후, 수정하기
                            .region(IntegratedRouteStatus.MULTI_REGION)
                            .build();
                    return integratedRouteRepository.save(newRoute);
                });

        return integratedRoute;
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
            //TODO: 나중에 커스텀 exception으로 바꾸기
            throw new RuntimeException("SHA-1 algorithm not found", e);
        }
    }

    private void saveUUIDHashtag(List<Long> hashtagIds, IntegratedRoute integratedRoute){
        // 현재 db에서 integratedRoute와 연관된 해시태그 찾기(이미 저장돼있는 해시태그)
        List<Long> dbHashtagIds = uuidHashtagRepository.findHashtagIdsByIntegratedRouteId(integratedRoute.getId());

        // 저장되어 있지 않아 새롭게 추가해야하는 해시태그 추출
        List<Long> newHashtagIds = hashtagIds.stream()
                .filter(id -> !dbHashtagIds.contains(id))
                .collect(Collectors.toList());

        // 추가해야하는 해시태그 아이디들의 객체 찾기
        List<Hashtag> hashtags = newHashtagIds.stream()
                .map(hashtagRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());


        for(Hashtag hashtag : hashtags){
            UUIDHashtag uuidHashtag = UUIDHashtag.builder()
                    .hashtag(hashtag)
                    .integratedRoute(integratedRoute)
                    .build();
            uuidHashtagRepository.save(uuidHashtag);
        }
    }


}
