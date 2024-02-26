package src.dkp.bot.strategy.processor.command.registration.menu;

import discord4j.core.event.domain.Event;
import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import discord4j.core.object.component.TextInput;
import discord4j.core.spec.InteractionPresentModalSpec;
import feign.Headers;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import src.dkp.bot.handler.UserInputStateHandler;
import src.dkp.bot.strategy.processor.command.MenuProcessingStrategy;

@Service
@RequiredArgsConstructor
public class RegistrationMessageProcessor implements MenuProcessingStrategy {

	private final UserInputStateHandler userInputStateHandler;

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

		TextInput nickname = TextInput.paragraph("nickname", "Игровой ник", 3, 20);

		// Создаем спецификацию модального окна с текстовым полем
		InteractionPresentModalSpec modalSpec = InteractionPresentModalSpec.builder()
				.customId("registration")
				.title("Введите ваш ник")
				.components(ActionRow.of(nickname)) // Добавляем текстовое поле в модальное окно
				.build();

		return buttonInteractionEvent.presentModal(modalSpec);

	}

}
