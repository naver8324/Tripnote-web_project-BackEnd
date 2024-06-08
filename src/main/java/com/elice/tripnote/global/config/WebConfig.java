package com.elice.tripnote.global.config;

import com.elice.tripnote.global.interceptor.AdminInterceptor;
import com.elice.tripnote.global.interceptor.MemberInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AdminInterceptor adminInterceptor;
    private final MemberInterceptor memberInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/api/**");

        registry.addInterceptor(memberInterceptor)
                .addPathPatterns("/api/**");
    }
}