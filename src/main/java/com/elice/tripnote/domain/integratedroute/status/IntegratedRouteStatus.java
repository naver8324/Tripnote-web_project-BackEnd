package com.elice.tripnote.domain.integratedroute.status;

import com.elice.tripnote.domain.integratedroute.exception.EntityNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum IntegratedRouteStatus {
    SEOUL("서울"),
    BUSAN("부산"),
    DAEGU("대구"),
    INCHEON("인천"),
    GWANGJU("광주"),
    DAEJEON("대전"),
    ULSAN("울산"),
    SEJONG("세종"),
    GYEONGGI("경기"),
    GANGWON("강원"),
    CHUNGCHEONG_BUK("충북"),
    CHUNGCHEONG_NAM("충남"),
    JEOLLA_BUK("전북"),
    JEOLLA_NAM("전남"),
    GYEONGSANG_BUK("경북"),
    GYEONGSANG_NAM("경남"),
    JEJU("제주"),
    MULTI_REGION("2개 이상의 지역");

    private final String name;

    public static IntegratedRouteStatus fromName(String name){
        for (IntegratedRouteStatus status : IntegratedRouteStatus.values()) {
            if (status.getName().equals(name)) {
                return status;
            }
        }
        throw new EntityNotFoundException("Unknown name: " + name);
    }
}
