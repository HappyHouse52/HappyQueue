package com.happyhouse.HappyQueue.repositories;

import com.happyhouse.HappyQueue.model.QueueDb;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface QueueRepository extends CrudRepository<QueueDb, Integer> {
  @Query(value = "SELECT queue_db.* FROM queue_db WHERE queue_db.name = :name", nativeQuery = true)
  Optional<QueueDb> findByName(@Param("name") String name);
}
