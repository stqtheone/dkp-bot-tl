package src.dkp.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DkpDiscordBotApp {

	public static void main(String[] args) {
		SpringApplication.run(DkpDiscordBotApp.class);

	}

}
