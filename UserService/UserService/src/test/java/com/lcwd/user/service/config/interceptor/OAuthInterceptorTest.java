package com.lcwd.user.service.config.interceptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;

import com.hotel.user.service.config.interceptor.ClientCredentialsTokenProvider;
import com.hotel.user.service.config.interceptor.FeignClientInterceptor;
import com.hotel.user.service.config.interceptor.RestTemplateInterceptor;

import feign.RequestTemplate;

class OAuthInterceptorTest {

	@Test
	void feignInterceptorSetsBearerToken() {
		ClientCredentialsTokenProvider tokenProvider = mock(ClientCredentialsTokenProvider.class);
		when(tokenProvider.getAccessToken()).thenReturn("access-token");
		RequestTemplate template = new RequestTemplate();

		new FeignClientInterceptor(tokenProvider).apply(template);

		assertEquals("Bearer access-token", template.headers().get(HttpHeaders.AUTHORIZATION).iterator().next());
	}

	@Test
	void restTemplateInterceptorSetsBearerToken() throws Exception {
		ClientCredentialsTokenProvider tokenProvider = mock(ClientCredentialsTokenProvider.class);
		when(tokenProvider.getAccessToken()).thenReturn("access-token");
		HttpRequest request = mock(HttpRequest.class);
		ClientHttpRequestExecution execution = mock(ClientHttpRequestExecution.class);
		ClientHttpResponse response = mock(ClientHttpResponse.class);
		HttpHeaders headers = new HttpHeaders();
		byte[] body = new byte[0];
		when(request.getHeaders()).thenReturn(headers);
		when(execution.execute(request, body)).thenReturn(response);

		ClientHttpResponse actualResponse = new RestTemplateInterceptor(tokenProvider).intercept(request, body,
				execution);

		assertEquals("Bearer access-token", headers.getFirst(HttpHeaders.AUTHORIZATION));
		assertEquals(response, actualResponse);
		verify(execution).execute(request, body);
	}
}
