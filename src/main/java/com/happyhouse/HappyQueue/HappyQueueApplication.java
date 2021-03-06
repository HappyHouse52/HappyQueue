package com.happyhouse.HappyQueue;

import com.happyhouse.HappyQueue.model.QueueDb;
import com.happyhouse.HappyQueue.model.TrackDb;
import com.happyhouse.HappyQueue.model.UserDb;
import com.happyhouse.HappyQueue.repositories.QueueRepository;
import com.happyhouse.HappyQueue.repositories.UserRepository;
import java.util.Optional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class HappyQueueApplication implements CommandLineRunner {
	private final QueueRepository queueRepository;
	private final UserRepository userRepository;

	public HappyQueueApplication(QueueRepository queueRepository, UserRepository userRepository) {
		this.queueRepository = queueRepository;
		this.userRepository = userRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(HappyQueueApplication.class, args);
	}

	@Override
	public void run(String... args) {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		Optional<QueueDb> queue = queueRepository.findByName("test-queue");
		if (queue.isEmpty()) {
			queueRepository.save(new QueueDb("test-queue", new TrackDb("Nice For What", "by Drake", "https://i.scdn.co/image/e6d17a95cac4810507dfb0a8242e4463d4c32a30", "spotify:track:3CA9pLiwRIGtUBiMjbZmRw", "dbannon")));
		}
		UserDb userDb = userRepository.findByUsername("dbannon");
    if (userDb == null) {
      userRepository.save(new UserDb("dbannon", "password", passwordEncoder.encode("password")));
		}
	}
}
