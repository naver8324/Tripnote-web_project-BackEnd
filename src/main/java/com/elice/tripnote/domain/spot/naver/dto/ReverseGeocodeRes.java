package com.elice.tripnote.domain.spot.naver.dto;

import lombok.Data;
import java.util.List;

@Data
public class ReverseGeocodeRes {
    private List<Result> results;

    @Data
    public static class Result {
        private Region region;
        private Land land;

        @Data
        public static class Region {
            private Area area1;
            private Area area2;
            private Area area3;
            private Area area4;

            @Data
            public static class Area {
                private String name;
            }
        }

        @Data
        public static class Land {
            private String type;
            private String number1;
            private String number2;
        }
    }
}
