package com.happyhouse.HappyQueue;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class HappyQueueApplication {
	@Value("${SPOTIFY_CLIENT_ID}")
	private String clientId;
	@Value("${SPOTIFY_CLIENT_SECRET}")
	private String clientSecret;

	@Bean
	public SpotifyApi spotifyApi() {

		final SpotifyApi spotifyApi = SpotifyApi.builder()
				.setClientId(clientId)
				.setClientSecret(clientSecret)
				.build();
		final ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials().build();
		try {
			final ClientCredentials credentials = clientCredentialsRequest.execute();
			spotifyApi.setAccessToken(credentials.getAccessToken());
			return spotifyApi;
		} catch (IOException | SpotifyWebApiException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(HappyQueueApplication.class, args);
	}
}
