package src.dkp.bot.strategy.processor.command.dkp.menu;

import discord4j.core.event.domain.Event;
import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import src.dkp.bot.strategy.processor.command.MenuProcessingStrategy;

@Service
public class DkpStatMenuMessageProcessor implements MenuProcessingStrategy {

	private final String DKP_STAT = "dkp_stat";
	private final String BACK_TO_DKP_STAT = "back_to_dkp_stat";

	@Override
	public boolean appliesTo(Event event) {
		if (event instanceof ButtonInteractionEvent buttonInteractionEvent) {
			return buttonInteractionEvent.getCustomId().equals(BACK_TO_DKP_STAT) || buttonInteractionEvent.getCustomId().equals(DKP_STAT);
		}
		return false;

	}

	@Override
	public Mono<Void> process(Event event) {
		return ((ButtonInteractionEvent) event).edit()
				.withComponents(ActionRow.of(
						Button.primary("user_dkp_stat", "Ваши очки ДКП"),
						Button.primary("all_dkp_stat", "Общие очки ДКП"),
						Button.primary("back_to_dkp", "Назад")
				))
				.withContent("")
				.withEphemeral(true);

	}

}
