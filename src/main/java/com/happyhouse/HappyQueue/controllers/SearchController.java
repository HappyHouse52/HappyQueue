package com.happyhouse.HappyQueue.controllers;

import com.happyhouse.HappyQueue.model.SearchResponse;
import com.happyhouse.HappyQueue.model.Track;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Image;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import com.wrapper.spotify.requests.data.search.simplified.SearchTracksRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {
  private static final Logger LOG = LoggerFactory.getLogger(SearchController.class);
  @Value("${spotify.client.id}")
  private String clientId;
  @Value("${spotify.client.secret}")
  private String clientSecret;

  @GetMapping(path = "/v1/search")
  public SearchResponse search(@RequestParam(name = "q") String query) {
    final SpotifyApi spotifyApi = getSpotifyApi();
    final SearchTracksRequest searchRequest = spotifyApi.searchTracks(query).build();
    final List<com.wrapper.spotify.model_objects.specification.Track> results;
    try {
      results = Arrays.asList(searchRequest.execute().getItems());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return SearchResponse.of(results.stream().map(this::fromSpotifyModel).collect(Collectors.toList()));
  }

  private Track fromSpotifyModel(com.wrapper.spotify.model_objects.specification.Track track) {
    return Track.builder()
        .setTitle(track.getName())
        .setImageUrl(Stream.of(track.getAlbum().getImages())
            .map(Image::getUrl)
            .findFirst().orElseThrow())
        .setSubtitle("By " + Stream.of(track.getArtists())
            .map(ArtistSimplified::getName)
            .collect(Collectors.joining(", ")))
        .setSpotifyUri(track.getUri())
        .build();
  }

  private SpotifyApi getSpotifyApi() {
    LOG.info("Building Spotify API");
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
}
