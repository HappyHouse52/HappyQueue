package com.happyhouse.HappyQueue.controllers;

import com.happyhouse.HappyQueue.model.QueueDb;
import com.happyhouse.HappyQueue.model.TrackDb;
import com.happyhouse.HappyQueue.repositories.QueueRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.stream.Collectors;

@RestController
public class QueueController {
  private final QueueRepository queueRepository;

  public QueueController(QueueRepository queueRepository) {
    this.queueRepository = queueRepository;
  }

  @GetMapping("/v1/queue/{queueName}")
  public QueueDb getQueue(@PathVariable String queueName) {
    QueueDb queue = queueRepository.findByName(queueName)
        .orElseGet(() -> queueRepository.save(new QueueDb(queueName)));

    queue.setTracks(queue.getTracks()
        .stream()
        .sorted(Comparator.comparing(TrackDb::getQueueTime))
        .collect(Collectors.toList()));
    return queue;
  }
}
