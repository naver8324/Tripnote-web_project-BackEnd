package com.elice.tripnote.domain.member.service;

import com.elice.tripnote.global.jwt.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TokenBlacklistService {

    private final Set<String> blacklistedTokens = new HashSet<>();
    private final JWTUtil jwtUtil;

    public TokenBlacklistService(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public void addTokenToBlacklist(String token) {
        blacklistedTokens.add(token);
        log.info("블랙리스트에 추가 된 토큰 : "+token);

        // 토큰 만료 시간을 가져와서 지정된 시간 후에 토큰을 자동으로 제거하는 스레드
        // 예를 들어 토큰의 만료 시간이 10시간 후면 해당 시간이 지나면 토큰을 블랙리스트에서 자동으로 제거
        long expirationTimeMillis = jwtUtil.getExpirationTimeMillis(token);
        long currentMillis = new Date().getTime();
        long delay = expirationTimeMillis - currentMillis;

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(() -> {
            blacklistedTokens.remove(token);
        }, delay, TimeUnit.MILLISECONDS);
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}