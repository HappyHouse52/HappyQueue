package com.happyhouse.HappyQueue.repositories;

import com.happyhouse.HappyQueue.model.TrackDb;
import com.happyhouse.HappyQueue.model.VoteDb;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface VoteRepository extends CrudRepository<VoteDb, Integer> {
  List<VoteDb> findByTrack(TrackDb trackDb);
  Optional<VoteDb> findByUsernameAndTrack(String username, TrackDb trackDb);
}
