package src.dkp.bot.config;

import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.gateway.intent.IntentSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import src.dkp.bot.listener.EventListener;

import java.util.List;

@Configuration
public class BotConfiguration {

	@Value("${token}")
	private String token;

	@Bean
	public <T extends Event> GatewayDiscordClient gatewayDiscordClient(final List<EventListener<T>> eventListeners) {
		final GatewayDiscordClient client = DiscordClient.builder(token)
				.build()
				.gateway()
				.setEnabledIntents(IntentSet.all())
				.login()
				.block();

		registerCommands(client);

		for (final EventListener<T> listener : eventListeners) {
			client.on(listener.getEventType())
					.flatMap(listener::execute)
					.onErrorResume(listener::handleError)
					.subscribe();
		}

		return client;
	}

	public void registerCommands(GatewayDiscordClient client) {
		long applicationId = client.getRestClient().getApplicationId().block();

		ApplicationCommandRequest menu = ApplicationCommandRequest.builder()
				.name("menu")
				.description("Меню")
				.build();

		client.getRestClient().getApplicationService()
				.createGuildApplicationCommand(applicationId,1207997792761675796L, menu)
				.doOnError(e -> System.out.println("Failed to create command: " + e.getMessage()))
				.subscribe();
	}
}