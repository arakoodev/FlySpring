package com.htmx.thymeleaf;

import com.htmx.thymeleaf.users.Info;
import com.htmx.thymeleaf.users.InfoRepo;
import com.htmx.thymeleaf.users.User;
import com.htmx.thymeleaf.users.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

@SpringBootApplication
public class HtmxApplication implements CommandLineRunner {
	@Autowired private UserRepo userRepo;
	@Autowired private InfoRepo infoRepo;

	public static void main(String[] args) {
		SpringApplication.run(HtmxApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -10);
		var random = new Random();
		for (int i = 1; i <= 10; i++) {
			// populate users
			var name = "User name %d".formatted(i);
			var email = "test_email%d@user.com".formatted(i);
			User user = new User(name, email);
			userRepo.save(user);

			// populate info
			Info info = new Info(
					dateFormat.format(calendar.getTime()),
					random.nextInt(10),
					random.nextInt(10)
			);
			infoRepo.save(info);
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}
	}
}
