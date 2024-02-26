package src.dkp.bot.strategy.processor.command.registration.message;

import discord4j.core.event.domain.Event;
import discord4j.core.event.domain.interaction.ModalSubmitInteractionEvent;
import discord4j.discordjson.json.ComponentData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import src.dkp.bot.service.player.PlayerService;
import src.dkp.bot.strategy.processor.command.MenuProcessingStrategy;

import java.util.List;

import static src.dkp.bot.service.client.DiscordClientHelper.getValueFromModalComponent;

@RequiredArgsConstructor
@Service
public class RegistrationModalWindowProcessor implements MenuProcessingStrategy {
	private final PlayerService playerService;


	private final String REGISTRATION = "registration";

	@Override
	public boolean appliesTo(Event event) {
		if (event instanceof ModalSubmitInteractionEvent modalSubmitInteractionEvent) {
			return modalSubmitInteractionEvent.getCustomId().equals(REGISTRATION);
		}
		return false;

	}

	@Override
	public Mono<Void> process(Event event) {
		if (event instanceof ModalSubmitInteractionEvent modalSubmitInteractionEvent) {
			String nickname = getValueFromModalComponent(modalSubmitInteractionEvent, "nickname");

			if (nickname != null) {
				String username = modalSubmitInteractionEvent.getMessage().get().getAuthor().get().getUsername();
				String discordId = modalSubmitInteractionEvent.getMessage().get().getAuthor().get().getId().asString();
				playerService.createPlayer(nickname, discordId, username);
				return modalSubmitInteractionEvent.reply("Заявка на регистрацию отправлена на рассмотрение администратору сервера").then();
			} else {
				return modalSubmitInteractionEvent.reply("Некорректный формат данных").then();

			}

		}
		return Mono.empty();

	}


}
