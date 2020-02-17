package com.happyhouse.HappyQueue.repositories;

import com.happyhouse.HappyQueue.model.QueueDb;
import com.happyhouse.HappyQueue.model.TrackDb;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TrackRepository extends CrudRepository<TrackDb, Integer> {
  Optional<TrackDb> findByIdAndQueue(Integer id, QueueDb queue);
}
