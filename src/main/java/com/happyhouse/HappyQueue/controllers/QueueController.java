package com.happyhouse.HappyQueue.controllers;

import com.happyhouse.HappyQueue.model.SpotifyQueue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QueueController {
  @GetMapping("/v1/queues")
  public SpotifyQueue getQueue(@RequestParam(name = "id", defaultValue = "test") String queueId) {
    return SpotifyQueue.builder().setId(queueId).build();
  }
}
