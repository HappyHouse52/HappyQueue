package com.happyhouse.HappyQueue.controllers;

import com.happyhouse.HappyQueue.model.User;
import com.happyhouse.HappyQueue.model.UserDb;
import com.happyhouse.HappyQueue.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
  private final UserRepository userRepository;

  public UserController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @PostMapping("/v1/users")
  public UserDb createUser(@RequestBody User user) {
    PasswordEncoder encoder = new BCryptPasswordEncoder();
    UserDb forDb = new UserDb(user.getUsername(), user.getPassword(), encoder.encode(user.getPassword()));
    return userRepository.save(forDb);
  }
}
