package com.mawsitsit;

import com.mawsitsit.Model.Hearthbeat;
import com.mawsitsit.Repository.HearthbeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookingresourceApplication implements CommandLineRunner {

	@Autowired
	private
	HearthbeatRepository hearthbeatRepo;

	public static void main(String[] args) {
		SpringApplication.run(BookingresourceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		hearthbeatRepo.save(new Hearthbeat(true));
	}
}
