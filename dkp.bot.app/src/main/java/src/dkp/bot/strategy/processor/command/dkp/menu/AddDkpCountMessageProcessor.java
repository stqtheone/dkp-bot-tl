package src.dkp.bot.strategy.processor.command.dkp.menu;

import discord4j.core.event.domain.Event;
import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.TextInput;
import discord4j.core.spec.InteractionPresentModalSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import src.dkp.bot.handler.RegistrationEventStateHandler;
import src.dkp.bot.handler.UserInputStateHandler;
import src.dkp.bot.strategy.processor.command.MenuProcessingStrategy;

@RequiredArgsConstructor
@Service
public class AddDkpCountMessageProcessor implements MenuProcessingStrategy {

	private final String ADD_DKP = "add_dkp";

	@Override
	public boolean appliesTo(Event event) {
		if (event instanceof ButtonInteractionEvent buttonInteractionEvent) {
			return buttonInteractionEvent.getCustomId().equals(ADD_DKP);
		}
		return false;

	}

	@Override
	public Mono<Void> process(Event event) {

		/// TODO: 18.02.2024 доделать проверку на канал ввода сообщения по регистрации

		ButtonInteractionEvent buttonInteractionEvent = (ButtonInteractionEvent) event;

		TextInput count = TextInput.paragraph("dkp_count", "Количество очков",3, 20);
		TextInput nickname = TextInput.paragraph("nickname", "Игровой ник", 3, 20);

		// Создаем спецификацию модального окна с текстовым полем
		InteractionPresentModalSpec modalSpec = InteractionPresentModalSpec.builder()
				.customId("add_dkp")
				.title("Введите ваш ник")
				.components(ActionRow.of(count), ActionRow.of(nickname)) // Добавляем текстовое поле в модальное окно
				.build();


		return buttonInteractionEvent.presentModal(modalSpec).then();

	}

}
