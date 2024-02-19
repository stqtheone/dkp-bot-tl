package src.dkp.bot.strategy.processor.command;

import discord4j.core.event.domain.Event;
import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import src.dkp.bot.handler.UserInputStateHandler;

@Service
public class RegistrationMessageProcessor implements MenuProcessingStrategy {
	@Autowired
	private UserInputStateHandler userInputStateHandler;

	private final String DKP_REGISTRATION = "dkp_registration";

	@Override
	public boolean appliesTo(Event event) {
		if (event instanceof ButtonInteractionEvent buttonInteractionEvent) {
			return buttonInteractionEvent.getCustomId().equals(DKP_REGISTRATION);
		}
		return false;

	}

	@Override
	public Mono<Void> process(Event event) {

		/// TODO: 18.02.2024 доделать проверку на канал ввода сообщения по регистрации

		ButtonInteractionEvent buttonInteractionEvent = (ButtonInteractionEvent) event;

		String userId = buttonInteractionEvent.getInteraction().getUser().getId().asString();

		userInputStateHandler.setAwaitingInput(userId, true);

		return buttonInteractionEvent.reply("Пожалуйста, введите в чат ваш игровой ник")
				.withEphemeral(true);

	}

}
