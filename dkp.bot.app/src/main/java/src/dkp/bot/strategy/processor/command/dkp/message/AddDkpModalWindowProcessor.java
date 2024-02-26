package src.dkp.bot.strategy.processor.command.dkp.message;

import discord4j.core.event.domain.Event;
import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.core.event.domain.interaction.ModalSubmitInteractionEvent;
import discord4j.core.object.component.TextInput;
import discord4j.core.object.entity.User;
import discord4j.discordjson.json.ApplicationCommandInteractionData;
import discord4j.discordjson.json.ComponentData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import src.dkp.bot.entity.Player;
import src.dkp.bot.service.player.PlayerService;
import src.dkp.bot.strategy.processor.command.MenuProcessingStrategy;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static src.dkp.bot.service.client.DiscordClientHelper.getValueFromModalComponent;

@RequiredArgsConstructor
@Service
public class AddDkpModalWindowProcessor implements MenuProcessingStrategy {
	private final PlayerService playerService;


	private final String ADD_DKP = "add_dkp";

	@Override
	public boolean appliesTo(Event event) {
		if (event instanceof ModalSubmitInteractionEvent modalSubmitInteractionEvent) {
			return modalSubmitInteractionEvent.getCustomId().equals(ADD_DKP);
		}
		return false;

	}

	@Override
	public Mono<Void> process(Event event) {
		if (event instanceof ModalSubmitInteractionEvent modalSubmitInteractionEvent) {
			String nickname = getValueFromModalComponent(modalSubmitInteractionEvent, "nickname");
			String count = getValueFromModalComponent(modalSubmitInteractionEvent, "dkp_count");

			if (count != null && nickname != null) {
				Optional<Player> player = playerService.addDkpCount(nickname, Long.parseLong(count));
				if (player.isPresent()) {
					return modalSubmitInteractionEvent.reply("Очки успешно начислены").then();
				} else {
					return modalSubmitInteractionEvent.reply("Пользователь не найден").then();
				}
			} else {
				return modalSubmitInteractionEvent.reply("Некорректный формат данных").then();

			}

		}
		return Mono.empty();

	}


}
