package com.happyhouse.HappyQueue.controllers;

import com.happyhouse.HappyQueue.model.SpotifyQueue;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Image;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.data.search.simplified.SearchTracksRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QueueController {
  private final SpotifyApi spotifyApi;

  public QueueController(final SpotifyApi spotifyApi) {this.spotifyApi = spotifyApi;}

  @GetMapping("/v1/queues")
  public SpotifyQueue getQueue(@RequestParam(name = "id", defaultValue = "test") String queueId) {
    final SearchTracksRequest searchRequest = spotifyApi.searchTracks(queueId).build();
    final List<Track> results;
    try {
      results = Arrays.asList(searchRequest.execute().getItems());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return SpotifyQueue.builder()
        .setId(queueId)
        .setTracks(results.stream().map(this::fromSpotifyModel).collect(Collectors.toList()))
        .build();
  }

  private com.happyhouse.HappyQueue.model.Track fromSpotifyModel(Track track) {
    return com.happyhouse.HappyQueue.model.Track.builder()
        .setName(track.getName())
        .setImageUrl(Stream.of(track.getAlbum().getImages()).map(Image::getUrl).findFirst().orElseThrow())
        .setSubtitle(Stream.of(track.getArtists()).map(ArtistSimplified::getName).collect(Collectors.joining(", ")))
        .build();
  }
}
