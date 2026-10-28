package com.lcwd.user.service.config.interceptor;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ClientCredentialsTokenProviderTest {

    @Test
    void returnsAccessTokenForInternalClient() {
        OAuth2AuthorizedClientManager manager = mock(OAuth2AuthorizedClientManager.class);
        OAuth2AccessToken accessToken = new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,
                "access-token",
                Instant.now(),
                Instant.now().plusSeconds(300)
        );
        ClientRegistration registration = ClientRegistration
                .withRegistrationId(ClientCredentialsTokenProvider.CLIENT_REGISTRATION_ID)
                .clientId("client-id")
                .clientSecret("client-secret")
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .tokenUri("https://issuer.example/oauth2/token")
                .build();
        when(manager.authorize(any(OAuth2AuthorizeRequest.class)))
                .thenReturn(new OAuth2AuthorizedClient(registration, "user-service", accessToken));

        ClientCredentialsTokenProvider provider = new ClientCredentialsTokenProvider(manager);

        assertEquals("access-token", provider.getAccessToken());

        ArgumentCaptor<OAuth2AuthorizeRequest> requestCaptor =
                ArgumentCaptor.forClass(OAuth2AuthorizeRequest.class);
        verify(manager).authorize(requestCaptor.capture());
        assertEquals(
                ClientCredentialsTokenProvider.CLIENT_REGISTRATION_ID,
                requestCaptor.getValue().getClientRegistrationId()
        );
        assertEquals("user-service", requestCaptor.getValue().getPrincipal().getName());
    }

    @Test
    void failsClearlyWhenAuthorizationDoesNotReturnAClient() {
        OAuth2AuthorizedClientManager manager = mock(OAuth2AuthorizedClientManager.class);
        when(manager.authorize(any(OAuth2AuthorizeRequest.class))).thenReturn(null);
        ClientCredentialsTokenProvider provider = new ClientCredentialsTokenProvider(manager);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                provider::getAccessToken
        );

        assertTrue(exception.getMessage().contains(ClientCredentialsTokenProvider.CLIENT_REGISTRATION_ID));
    }
}
