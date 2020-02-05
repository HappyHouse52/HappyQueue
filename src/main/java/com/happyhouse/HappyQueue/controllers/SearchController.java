package com.happyhouse.HappyQueue.controllers;

import com.happyhouse.HappyQueue.model.SearchResponse;
import com.happyhouse.HappyQueue.model.Track;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Image;
import com.wrapper.spotify.requests.data.search.simplified.SearchTracksRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class SearchController {
  private SpotifyApi spotifyApi;

  public SearchController(SpotifyApi spotifyApi) {
    this.spotifyApi = spotifyApi;
  }

  @GetMapping(path = "/v1/search")
  public SearchResponse search(@RequestParam(name = "q") String query) {
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
}
