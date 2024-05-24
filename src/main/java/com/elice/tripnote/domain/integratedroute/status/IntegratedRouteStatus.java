package com.elice.tripnote.domain.integratedroute.status;

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
    CHUNGCHEONGBUK("충북"),
    CHUNGCHEONGNAM("충남"),
    JEOLLABUK("전북"),
    JEOLLANAM("전남"),
    GYEONGSANGBUK("경북"),
    GYEONGSANGNAM("경남"),
    JEJU("제주"),
    MULTI_REGION("2개 이상의 지역");

    private final String name;
}
