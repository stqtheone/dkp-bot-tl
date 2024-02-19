package src.dkp.bot.strategy.processor.command;

import discord4j.core.event.domain.Event;
import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class MainMenuMessageProcessor implements MenuProcessingStrategy {

	private final String MENU = "menu";
	private final String BACK_TO_MENU = "back_to_menu";

	@Override
	public boolean appliesTo(Event event) {
		if (event instanceof ChatInputInteractionEvent chatInputInteractionEvent) {
			return chatInputInteractionEvent.getCommandName().equals(MENU);
		} else if (event instanceof ButtonInteractionEvent buttonInteractionEvent) {
			return buttonInteractionEvent.getCustomId().equals(BACK_TO_MENU);
		}
		return false;

	}

	@Override
	public Mono<Void> process(Event event) {
		if (event instanceof ChatInputInteractionEvent chatInputEvent) { // Создаем меню
			return chatInputEvent.reply()
					.withComponents(ActionRow.of(
							Button.primary("dkp", "ДКП система"), // Создаем кнопку с идентификатором "dkp"
							Button.primary("auction", "Аукцион"), // Создаем кнопку с идентификатором "auction"
							Button.primary("create_event", "Создать событие") // Создаем кнопку с идентификатором "auction"
					))
					.withEphemeral(true);
		}  else if (event instanceof ButtonInteractionEvent buttonInteractionEvent) { // Возвращаемся в меню если вызвано "Возврат в меню"
			return buttonInteractionEvent.edit()
					.withComponents(ActionRow.of(
							Button.primary("dkp", "ДКП система"), // Создаем кнопку с идентификатором "dkp"
							Button.primary("auction", "Аукцион"), // Создаем кнопку с идентификатором "auction"
							Button.primary("create_event", "Создать событие") // Создаем кнопку с идентификатором "auction"
					)).withContent("").withEphemeral(true);
		}
		return Mono.empty();
	}

}
