package com.happyhouse.HappyQueue;

import com.happyhouse.HappyQueue.model.QueueDb;
import com.happyhouse.HappyQueue.model.TrackDb;
import com.happyhouse.HappyQueue.repositories.QueueRepository;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.util.Optional;

@SpringBootApplication
public class HappyQueueApplication implements CommandLineRunner {
	@Value("${SPOTIFY_CLIENT_ID}")
	private String clientId;
	@Value("${SPOTIFY_CLIENT_SECRET}")
	private String clientSecret;

	private final QueueRepository queueRepository;

	public HappyQueueApplication(QueueRepository queueRepository) {
		this.queueRepository = queueRepository;
	}

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

	@Override
	public void run(String... args) throws Exception {
		Optional<QueueDb> queue = queueRepository.findByName("test-queue");
		if (queue.isEmpty()) {
			queueRepository.save(new QueueDb("test-queue", new TrackDb("Nice For What", "by Drake", "https://i.scdn.co/image/e6d17a95cac4810507dfb0a8242e4463d4c32a30", "spotify:track:3CA9pLiwRIGtUBiMjbZmRw", "dbannon")));
		}
	}
}
