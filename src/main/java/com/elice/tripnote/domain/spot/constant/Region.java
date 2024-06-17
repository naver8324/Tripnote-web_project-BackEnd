package com.elice.tripnote.domain.spot.constant;

import com.elice.tripnote.global.exception.CustomException;
import com.elice.tripnote.global.exception.ErrorCode;

public enum Region {
    MIXED_REGION("여러지역", "mix"),
    SEOUL("서울특별시", "seoul"), INCHEON("인천광역시", "incheon"), BUSAN("부산광역시", "busan"),
    DAEGU("대구광역시", "daegu"), ULSAN("울산광역시", "ulsan"),
    GWANGJU("광주광역시", "gwangju"), DAEJEON("대전광역시", "daejeon"), SEJONG("세종특별자치시", "sejong"),
    GYEONGGI("경기도", "gyeonggi"), GANGWON("강원특별자치도", "gangwon"),
    CHUNGBUK("충청북도", "chungbuk"), CHUNGNAM("충청남도", "chungnam"), GYEONGBUK("경상북도", "gyeongbuk"),
    GYEONGNAM("경상남도", "gyeongnam"), JEONBUK("전라북도", "jeonbuk"), JEONNAM("전라남도", "jeonnam"),
    JEJU("제주특별자치도", "jeju"), ALL("전지역", "all");

    private String name;
    private String englishName;

    Region(String name, String englishName) {
        this.name = name;
        this.englishName = englishName;
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
        throw new CustomException(ErrorCode.NO_REGION);
    }

    public static Region englishToRegion(String englishName) {
        for (Region region : Region.values()) {
            if (region.englishName.equals(englishName)) {
                return region;
            }
        }
        throw new CustomException(ErrorCode.NO_REGION);
    }

    public int getIndex() {
        return this.ordinal();
    }
}
