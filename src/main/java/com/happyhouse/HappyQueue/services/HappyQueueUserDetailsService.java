package com.happyhouse.HappyQueue.services;

import com.happyhouse.HappyQueue.model.UserDb;
import com.happyhouse.HappyQueue.repositories.UserRepository;
import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class HappyQueueUserDetailsService implements UserDetailsService {
  private UserRepository userRepository;

  public HappyQueueUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
    UserDb user = Optional.ofNullable(userRepository.findByUsername(username))
        .orElseThrow(() -> new UsernameNotFoundException(username));
    return new HappyQueueUserPrinciple(user);
  }
}
