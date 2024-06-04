package com.elice.tripnote.domain.spot.constant;

import com.elice.tripnote.domain.spot.exception.RegionNotFoundException;
import com.elice.tripnote.global.exception.ErrorCode;

public enum Region {
    SEOUL("서울특별시"),INCHEON("인천광역시"),BUSAN("부산광역시"), DAEGU("대구광역시"),ULSAN("울산광역시"),
    GWANGJU("광주광역시"), DAEJEON("대전광역시"),
    GYEONGGI("경기도"), GANGWON("강원특별자치도"),
    CHUNGBUK("충청북도"),CHUNGNAM("충청남도"), GYEONGBUK("경상북도"),GYEONGNAM("경상남도"), JEONBUK("전라북도"),JEONNAM("전라남도"),
    JEJU("제주특별자치도"),
    MULTI_REGION("여러 지역");

    private String name;

    Region(String name){
        this.name =name;
    }
    public String getName() {
        return name;
    }

    public static Region fromString(String name) {
        for (Region region : Region.values()) {
            if (region.name.equals(name)) {
                return region;
            }
        }
        throw new RegionNotFoundException(ErrorCode.NO_REGION);
    }
}
