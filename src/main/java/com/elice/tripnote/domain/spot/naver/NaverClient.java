package com.elice.tripnote.domain.spot.naver;


import com.elice.tripnote.domain.spot.naver.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.util.List;

@Component
public class NaverClient {
    @Value("${naver.client.id}")
    private String naverClientId;

    @Value("${naver.client.secret}")
    private String naverClientSecret;

    @Value("${naver.url.search.local}")
    private String naverSearchLocal;

    @Value("${naver.url.search.image}")
    private String naverSearchImage;

    @Value("${naver.map.id}")
    private String naverMapId;

    @Value("${naver.map.secret}")
    private String naverMapSecret;
    public SearchLocalRes searchLocal(SearchLocalReq searchLocalReq){
        var uri = UriComponentsBuilder
                .fromUriString(naverSearchLocal)
                .queryParams(searchLocalReq.toMultiValueMap())
                .build()
                .encode()
                .toUri();

        var headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", naverClientId);
        headers.set("X-Naver-Client-Secret", naverClientSecret);
        headers.setContentType(MediaType.APPLICATION_JSON);

        var httpEntity = new HttpEntity<>(headers);
        var responseType = new ParameterizedTypeReference<SearchLocalRes>(){};


        var responseEntity = new RestTemplate()
                .exchange(
                        uri,
                        HttpMethod.GET,
                        httpEntity,
                        responseType
                );

        return responseEntity.getBody();
    }

    public SearchImageRes searchImage(SearchImageReq searchImageReq){
        var uri = UriComponentsBuilder
                .fromUriString(naverSearchImage)
                .queryParams(searchImageReq.toMultiValueMap())
                .build()
                .encode()
                .toUri();

        var headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", naverClientId);
        headers.set("X-Naver-Client-Secret", naverClientSecret);
        headers.setContentType(MediaType.APPLICATION_JSON);

        var httpEntity = new HttpEntity<>(headers);
        var responseType = new ParameterizedTypeReference<SearchImageRes>(){};


        var responseEntity = new RestTemplate()
                .exchange(
                        uri,
                        HttpMethod.GET,
                        httpEntity,
                        responseType
                );

        return responseEntity.getBody();
    }

    public ReverseGeocodeRes reverseGeocode(double lat, double lng) {
        String url = "https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc?coords=" + lng + "," + lat + "&output=json";

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-NCP-APIGW-API-KEY-ID", naverMapId);
        headers.set("X-NCP-APIGW-API-KEY", naverMapSecret);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return new RestTemplate().exchange(url, HttpMethod.GET, entity, ReverseGeocodeRes.class).getBody();
    }

    public double[] geocoding(String query) {
        String geocodingUrl = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(geocodingUrl)
                .queryParam("query", query);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-NCP-APIGW-API-KEY-ID", naverMapId);
        headers.set("X-NCP-APIGW-API-KEY", naverMapSecret);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<GeocodingResponse> responseEntity = new RestTemplate()
                    .exchange(builder.toUriString(), HttpMethod.GET, entity, GeocodingResponse.class);

            GeocodingResponse geocodingResponse = responseEntity.getBody();
            System.out.println("Geocoding API Response: " + responseEntity);
            if (geocodingResponse != null && geocodingResponse.getAddresses() != null) {
                if (!geocodingResponse.getAddresses().isEmpty()) {
                    double x = geocodingResponse.getAddresses().get(0).getY();
                    double y = geocodingResponse.getAddresses().get(0).getX();
                    return new double[]{y, x};  // 네이버 API는 x가 경도, y가 위도입니다.
                } else {
                    System.out.println("No coordinates found for the given query: " + query);
                }
            } else {
                System.out.println("Geocoding API response is null or contains no addresses for query: " + query);
            }
        } catch (Exception e) {
            System.out.println("Exception occurred while calling Geocoding API: " + e.getMessage());
        }
        return new double[]{-1, -1}; // Default value for error case
    }
    private static class GeocodingResponse {
        private List<Address> addresses;

        public List<Address> getAddresses() {
            return addresses;
        }

        public void setAddresses(List<Address> addresses) {
            this.addresses = addresses;
        }

        public static class Address {
            private double x; // Longitude
            private double y; // Latitude

            public double getX() {
                return x;
            }

            public void setX(double x) {
                this.x = x;
            }

            public double getY() {
                return y;
            }

            public void setY(double y) {
                this.y = y;
            }
        }
    }

    public double[] convertMapXYToLatLon(double mapX, double mapY) {
        String conversionUrl = "https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(conversionUrl)
                .queryParam("coords", mapX + "," + mapY)
                .queryParam("sourcecrs", "epsg:5178") // KATECH
                .queryParam("targetcrs", "epsg:4326") // WGS84
                .queryParam("orders", "legalcode,admcode,addr,roadaddr")
                .queryParam("output", "json");

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-NCP-APIGW-API-KEY-ID", naverMapId);
        headers.set("X-NCP-APIGW-API-KEY", naverMapSecret);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<GeocodeRes> responseEntity = new RestTemplate()
                .exchange(builder.toUriString(), HttpMethod.GET, entity, GeocodeRes.class);
        GeocodeRes conversionResponse = responseEntity.getBody();

        if (conversionResponse != null && conversionResponse.getResults() != null) {
            if (!conversionResponse.getResults().isEmpty()) {
                double latitude = conversionResponse.getResults().get(0).getX();
                double longitude = conversionResponse.getResults().get(0).getY();
                return new double[]{latitude, longitude};
            } else {
                System.out.println("No coordinates found for the given mapX and mapY.");
            }
        }
        return new double[]{-1, -1};
    }

}
