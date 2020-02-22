package com.happyhouse.HappyQueue.controllers;

import com.google.common.annotations.VisibleForTesting;
import com.happyhouse.HappyQueue.enums.SortType;
import com.happyhouse.HappyQueue.model.QueueDb;
import com.happyhouse.HappyQueue.model.TrackDb;
import com.happyhouse.HappyQueue.repositories.QueueRepository;
import com.happyhouse.HappyQueue.repositories.TrackRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
  public QueueDb getQueue(@PathVariable String queueName, @RequestParam("sortOrder") SortType sortType) {
    QueueDb queue = queueRepository.findByName(queueName)
        .orElseGet(() -> queueRepository.save(new QueueDb(queueName)));

    List<TrackDb> tracks = sortTracks(queue.getTracks(), sortType);

    queue.setTracks(tracks);
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

  private List<TrackDb> sortTracks(List<TrackDb> tracks, SortType sortType) {
    switch (sortType) {
      case QUEUE_TIME:
        return getQueueTimeSort(tracks);
      case TOP_RATED:
        return getTopVotedSort(tracks);
      case ROUND_ROBIN:
        return getRoundRobinOrder(tracks);
      default:
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid sort type: " + sortType.name());
    }
  }

  @VisibleForTesting
  public static List<TrackDb> getQueueTimeSort(List<TrackDb> tracks) {
    return tracks.stream()
        .sorted(Comparator.comparing(TrackDb::getQueueTime))
        .collect(Collectors.toList());
  }

  @VisibleForTesting
  public static List<TrackDb> getTopVotedSort(List<TrackDb> tracks) {
    return tracks.stream()
        .sorted(Comparator.comparing(TrackDb::getVoteTotal)
            .thenComparing(TrackDb::getQueueTime))
        .collect(Collectors.toList());
  }

  @VisibleForTesting
  public static List<TrackDb> getRoundRobinOrder(List<TrackDb> tracks) {
    final List<TrackDb> timeSorted = tracks.stream()
        .sorted(Comparator.comparing(TrackDb::getQueueTime))
        .collect(Collectors.toList());
    final Map<String, Queue<TrackDb>> collect =
        timeSorted.stream().collect(Collectors.groupingBy(TrackDb::getQueuer, Collectors.toCollection(LinkedList::new)));

    Deque<TrackDb> results = new LinkedList<>();
    boolean remaining = true;

    while (remaining) {
      remaining = false;
      for (String user : collect.keySet()) {
        TrackDb nextTrack = collect.get(user).poll();
        if (nextTrack == null) continue;
        results.add(nextTrack);
        remaining = true;
      }
    }

    return new ArrayList<>(results);
  }

  @Transactional
  public void removeTrack(final TrackDb t, QueueDb queue) {
    queue.getTracks().remove(t);
    t.setQueue(null);
    trackRepository.delete(t);
  }
}
