package src.dkp.bot.strategy.processor.command.dkp.menu;

import discord4j.core.event.domain.Event;
import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.EmbedCreateSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import src.dkp.bot.entity.Player;
import src.dkp.bot.handler.UserInputStateHandler;
import src.dkp.bot.service.player.PlayerService;
import src.dkp.bot.strategy.processor.command.MenuProcessingStrategy;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DkpStatMessageProcessor implements MenuProcessingStrategy {

	private final PlayerService playerService;

	private final String USER_DKP_STAT = "user_dkp_stat";
	private final String ALL_DKP_STAT = "all_dkp_stat";

	@Override
	public boolean appliesTo(Event event) {
		if (event instanceof ButtonInteractionEvent buttonInteractionEvent) {
			return buttonInteractionEvent.getCustomId().equals(USER_DKP_STAT) || buttonInteractionEvent.getCustomId().equalsIgnoreCase(ALL_DKP_STAT);
		}
		return false;

	}

	@Override
	public Mono<Void> process(Event event) {
		ButtonInteractionEvent buttonInteractionEvent = (ButtonInteractionEvent) event;

		if (buttonInteractionEvent.getCustomId().equalsIgnoreCase(ALL_DKP_STAT)) {

			return buttonInteractionEvent.acknowledge().then(Mono.defer(() -> {
				Mono<MessageChannel> channelMono = buttonInteractionEvent.getInteraction().getChannel();

				EmbedCreateSpec.Builder embedBuilder = EmbedCreateSpec.builder()
						.title("Статистика ДКП очков клана JustPlay");

				List<Player> playerList = playerService.getAllActiveAndConfirmedPlayers();

				for (Player player : playerList) {
					embedBuilder.addField(player.getGameName(), String.valueOf(player.getDkpCount()), false);
				}

				return channelMono.flatMap(channel -> channel.createMessage(embedBuilder.build()));
			})).then();
		} else {
			return buttonInteractionEvent.acknowledge().then(Mono.defer(() -> {
				Mono<MessageChannel> channelMono = buttonInteractionEvent.getInteraction().getChannel();

				Player player = playerService.getPlayerByDiscordId(buttonInteractionEvent.getInteraction().getUser().getId().asString());

				EmbedCreateSpec embedBuilder = EmbedCreateSpec.builder()
						.title("Ваши очки ДКП")
						.addField(player.getGameName(), String.valueOf(player.getDkpCount()), false)
						.build();

				return channelMono.flatMap(channel -> channel.createMessage(embedBuilder));
			})).then();

		}
	}

}
