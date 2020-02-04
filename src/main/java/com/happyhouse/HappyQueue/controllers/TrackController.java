package com.happyhouse.HappyQueue.controllers;

import com.happyhouse.HappyQueue.model.QueueDb;
import com.happyhouse.HappyQueue.model.TrackDb;
import com.happyhouse.HappyQueue.repositories.QueueRepository;
import com.happyhouse.HappyQueue.repositories.TrackRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

@RestController
public class TrackController {
  private final TrackRepository trackRepository;
  private final QueueRepository queueRepository;

  public TrackController(TrackRepository trackRepository, QueueRepository queueRepository) {
    this.trackRepository = trackRepository;
    this.queueRepository = queueRepository;
  }

  @PostMapping("/v1/queue/{queueName}/track")
  public TrackDb addTrackToQueue(@PathVariable("queueName") String queueName,
                                 @RequestParam("name") String trackName,
                                 @RequestParam("subtitle") String subtitle,
                                 @RequestParam("image_url") String imageUrl,
                                 @RequestParam("spotify_uri") String spotifyUri) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userName = authentication.getName();
    QueueDb queue = queueRepository.findByName(queueName)
        .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "No queue found for name: " + queueName));
    return trackRepository.save(new TrackDb(trackName, subtitle, imageUrl, spotifyUri, userName, queue));
  }
}
