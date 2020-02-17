package com.happyhouse.HappyQueue.controllers;

import com.happyhouse.HappyQueue.model.QueueDb;
import com.happyhouse.HappyQueue.model.TrackDb;
import com.happyhouse.HappyQueue.repositories.QueueRepository;
import com.happyhouse.HappyQueue.repositories.TrackRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class QueueController {
  private final QueueRepository queueRepository;
  private final TrackRepository trackRepository;

  public QueueController(QueueRepository queueRepository,
                         TrackRepository trackRepository) {
    this.queueRepository = queueRepository;
    this.trackRepository = trackRepository;
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

  @DeleteMapping("/v1/queue/{queueName}/next")
  public TrackDb getAndRemoveNextTrack(@PathVariable final String queueName) {
    QueueDb queue = queueRepository.findByName(queueName)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No queue with name: " + queueName));

    final Optional<TrackDb> nextTrack = queue.getTracks()
        .stream()
        .max(Comparator.comparing(TrackDb::getQueueTime));

    nextTrack.ifPresentOrElse(
        t -> removeTrack(t, queue),
        () -> {
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No tracks in queue with name: " + queueName);
        });
    return nextTrack.get();
  }

  @DeleteMapping("/v1/queue/{queueName}/track/{trackId}")
  public TrackDb removeTrackFromQueue(@PathVariable("queueName") String queueName,
                                      @PathVariable("trackId") Integer trackId) {
    QueueDb queue = queueRepository.findByName(queueName)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No queue found for name: " + queueName));
    final Optional<TrackDb> trackToRemove = trackRepository.findByIdAndQueue(trackId, queue);
    trackToRemove.ifPresentOrElse(
        t -> removeTrack(t, queue),
        () -> {
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No tracks with ID=%d in queue=%s", trackId, queueName));
        }
    );
    return trackToRemove.get();
  }


  @Transactional
  public void removeTrack(final TrackDb t, QueueDb queue) {
    queue.getTracks().remove(t);
    t.setQueue(null);
    trackRepository.delete(t);
  }
}
