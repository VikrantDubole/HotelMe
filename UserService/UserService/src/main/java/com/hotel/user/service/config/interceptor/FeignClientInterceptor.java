package com.lcwd.user.service.config.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

@Component
public class FeignClientInterceptor implements RequestInterceptor {

    private final ClientCredentialsTokenProvider tokenProvider;

    public FeignClientInterceptor(ClientCredentialsTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void apply(RequestTemplate template) {
        template.header("Authorization", "Bearer " + tokenProvider.getAccessToken());
    }
}
