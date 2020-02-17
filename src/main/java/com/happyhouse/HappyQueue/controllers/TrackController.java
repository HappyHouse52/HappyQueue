package com.happyhouse.HappyQueue.controllers;

import com.happyhouse.HappyQueue.model.QueueDb;
import com.happyhouse.HappyQueue.model.TrackDb;
import com.happyhouse.HappyQueue.model.Track;
import com.happyhouse.HappyQueue.repositories.QueueRepository;
import com.happyhouse.HappyQueue.repositories.TrackRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
                                 @RequestBody Track track) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userName = authentication.getName();

    QueueDb queue = queueRepository.findByName(queueName)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No queue found for name: " + queueName));
    return trackRepository.save(new TrackDb(track.getTitle(), track.getSubtitle(), track.getImageUrl(), track.getSpotifyUri(), userName, queue));
  }
}
