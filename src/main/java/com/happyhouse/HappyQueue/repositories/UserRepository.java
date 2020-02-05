package com.happyhouse.HappyQueue.repositories;

import com.happyhouse.HappyQueue.model.UserDb;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserDb, Long> {
  UserDb findByUsername(String username);
}
