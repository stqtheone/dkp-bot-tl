package src.dkp.bot.strategy.processor.command;

import discord4j.core.event.domain.Event;
import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import src.dkp.bot.strategy.processor.command.MenuProcessingStrategy;

@Service
public class DkpMenuMessageProcessor implements MenuProcessingStrategy {

	private final String DKP = "dkp";
	private final String BACK_TO_DKP = "back_to_dkp";

	@Override
	public boolean appliesTo(Event event) {
		if (event instanceof ButtonInteractionEvent buttonInteractionEvent) {
			return buttonInteractionEvent.getCustomId().equals(BACK_TO_DKP) || buttonInteractionEvent.getCustomId().equals(DKP);
		}
		return false;

	}

	@Override
	public Mono<Void> process(Event event) {
		return ((ButtonInteractionEvent) event).edit()
				.withComponents(ActionRow.of(
						Button.primary("dkp_registration", "Регистрация в дкп системе"), // Создаем кнопку с идентификатором "dkp"
						Button.primary("user_confirmation", "Подтверждение регистрации"), // Создаем кнопку с идентификатором "dkp"
						Button.primary("dkp_stat", "Узнать текущее состояние дкп"), // Создаем кнопку с идентификатором "auction"
						Button.primary("add_dkp", "Начислить очки ДКП"), // Создаем кнопку с идентификатором "auction"
						Button.primary("back_to_menu", "Вернуться в меню") // Создаем кнопку с идентификатором "auction"
				))
				.withContent("")
				.withEphemeral(true);

	}

}
