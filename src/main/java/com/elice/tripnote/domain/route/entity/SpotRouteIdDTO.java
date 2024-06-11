package com.elice.tripnote.domain.route.entity;

import com.elice.tripnote.domain.spot.constant.Region;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SpotRouteIdDTO {
    private Long routeId;
    private Long id;
    private String location;
    private String imageUrl;
    private Region region;
    private String address;
    private double lat;
    private double lng;
}
