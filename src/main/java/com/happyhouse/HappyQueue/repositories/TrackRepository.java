package com.happyhouse.HappyQueue.repositories;

import com.happyhouse.HappyQueue.model.TrackDb;
import org.springframework.data.repository.CrudRepository;

public interface TrackRepository extends CrudRepository<TrackDb, Integer> {
}
