package src.dkp.bot.strategy.processor.command;

import discord4j.core.event.domain.Event;
import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import src.dkp.bot.handler.RegistrationEventStateHandler;
import src.dkp.bot.handler.UserInputStateHandler;

@Service
@RequiredArgsConstructor
public class RegistrationEventMenuMessageProcessor implements MenuProcessingStrategy {
	private final RegistrationEventStateHandler registrationEventStateHandler;

	private final String EVENT_REGISTRATION = "event_registration";

	@Override
	public boolean appliesTo(Event event) {
		if (event instanceof ButtonInteractionEvent buttonInteractionEvent) {
			return buttonInteractionEvent.getCustomId().equals(EVENT_REGISTRATION);
		}
		return false;

	}

	@Override
	public Mono<Void> process(Event event) {

		/// TODO: 18.02.2024 доделать проверку на канал ввода сообщения по регистрации

		ButtonInteractionEvent buttonInteractionEvent = (ButtonInteractionEvent) event;

		String userId = buttonInteractionEvent.getInteraction().getUser().getId().asString();

		registrationEventStateHandler.setAwaitingInput(userId, true);

		return buttonInteractionEvent.reply("Пожалуйста, введите в чат код события")
				.withEphemeral(true);

	}

}
