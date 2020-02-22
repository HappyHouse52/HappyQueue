package com.happyhouse.HappyQueue.controllers;

import com.happyhouse.HappyQueue.enums.VoteType;
import com.happyhouse.HappyQueue.model.*;
import com.happyhouse.HappyQueue.repositories.QueueRepository;
import com.happyhouse.HappyQueue.repositories.TrackRepository;
import com.happyhouse.HappyQueue.repositories.VoteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class VoteController {
  private final TrackRepository trackRepository;
  private final VoteRepository voteRepository;
  private final QueueRepository queueRepository;

  public VoteController(TrackRepository trackRepository,
                        VoteRepository voteRepository,
                        QueueRepository queueRepository) {
    this.trackRepository = trackRepository;
    this.voteRepository = voteRepository;
    this.queueRepository = queueRepository;
  }

  @PostMapping("/v1/vote")
  public VoteResponse upVoteSong(@RequestBody VoteRequest request) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();

    String queueName = request.getQueueName();
    int trackId = request.getTrackId();
    VoteType voteType = request.getVoteType();

    QueueDb queue = queueRepository.findByName(queueName)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No queue found for name: " + queueName));
    TrackDb track = trackRepository.findByIdAndQueue(trackId, queue)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No track found for ID: " + trackId));
    VoteDb vote = voteRepository.findByUsernameAndTrack(username, track)
        .orElse(new VoteDb(voteType, username, track));

    vote.setVoteType(voteType);
    voteRepository.save(vote);

    List<VoteDb> votes = voteRepository.findByTrack(track);
    int voteTotal = votes.stream()
        .mapToInt(v -> v.getVoteType().getValue())
        .sum();

    return VoteResponse.of(true, voteTotal);
  }
}
