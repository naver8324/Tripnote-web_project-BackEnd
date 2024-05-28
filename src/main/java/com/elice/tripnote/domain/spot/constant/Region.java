package com.elice.tripnote.domain.spot.constant;

import com.elice.tripnote.domain.spot.exception.RegionNotFoundException;

public enum Region {
    SEOUL("서울특별시"),INCHEON("인천"),BUSAN("부산"), DAEGU("대구"),ULSAN("울산"),
    GWANGJU("광주"), DAEJEON("대전"),
    GYEONGGI("경기도"), GANGWON("강원도"),
    CHUNGBUK("충청북도"),CHUNGNAM("충청남도"), GYEONGBUK("경상북도"),GYEONGNAM("경상남도"), JEONBUK("전라북도"),JEONNAM("전라남도"),
    JEJU("제주특별자치도");

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
        throw new RegionNotFoundException();
    }
}
