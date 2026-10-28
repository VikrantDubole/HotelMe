package com.hotel.user.service.config.interceptor;

import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Component;

@Component
public class ClientCredentialsTokenProvider {

	public static final String CLIENT_REGISTRATION_ID = "my-internal-client";
	private static final String SERVICE_PRINCIPAL = "user-service";

	private final OAuth2AuthorizedClientManager authorizedClientManager;

	public ClientCredentialsTokenProvider(OAuth2AuthorizedClientManager authorizedClientManager) {
		this.authorizedClientManager = authorizedClientManager;
	}

	public String getAccessToken() {
		OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
				.withClientRegistrationId(CLIENT_REGISTRATION_ID).principal(SERVICE_PRINCIPAL).build();
		OAuth2AuthorizedClient authorizedClient = authorizedClientManager.authorize(authorizeRequest);

		if (authorizedClient == null || authorizedClient.getAccessToken() == null) {
			throw new IllegalStateException(
					"Unable to obtain an OAuth2 access token for client registration '" + CLIENT_REGISTRATION_ID + "'");
		}

		return authorizedClient.getAccessToken().getTokenValue();
	}
}
