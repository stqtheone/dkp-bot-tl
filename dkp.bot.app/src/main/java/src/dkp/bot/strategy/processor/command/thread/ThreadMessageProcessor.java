package src.dkp.bot.strategy.processor.command.thread;

import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.Channel;
import discord4j.core.object.entity.channel.ThreadChannel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import src.dkp.bot.entity.DkpEvent;
import src.dkp.bot.entity.Player;
import src.dkp.bot.service.event.EventService;
import src.dkp.bot.service.player.PlayerService;
import src.dkp.bot.strategy.processor.command.MessageProcessingStrategy;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ThreadMessageProcessor implements MessageProcessingStrategy {
	private final EventService eventService;
	private final PlayerService playerService;

	@Override
	public boolean appliesTo(MessageCreateEvent event) {
		Channel channel = event.getMessage().getChannel().block();

		if (channel instanceof ThreadChannel threadChannel) {

			String threadId = threadChannel.getId().asString();

			DkpEvent dkpEvent = eventService.findDkpEventByThreadId(threadId);

			return dkpEvent != null && !event.getMessage().getAuthor().get().isBot();
		}

		return false;
	}

	@Override
	public Mono<Void> process(MessageCreateEvent event) {
		return event.getMessage().getChannel().flatMap(channel -> {
			if (channel instanceof ThreadChannel threadChannel) {
				String threadId = threadChannel.getId().asString();

				DkpEvent dkpEvent = eventService.findDkpEventByThreadId(threadId);

				if (dkpEvent != null) {
					String discordUserId = event.getMessage().getAuthor().map(User::getId).map(Snowflake::asString).orElse(null);
					String username = event.getMessage().getAuthor().get().getUsername();

					if (discordUserId != null) {
						Player player = playerService.getPlayerByDiscordId(discordUserId);

						if (player != null) {
							List<DkpEvent> playerEvents = player.getEvents();

							if (!playerEvents.contains(dkpEvent)) {
								playerEvents.add(dkpEvent);

								player.setEvents(playerEvents);

								playerService.updatePlayer(player);

								return threadChannel.createMessage(username + " ваш скрин принят").then();

							} else {
								return event.getMessage().delete("Вы уже зарегистрированы в событии!").then();
							}
						} else {
							return threadChannel.createMessage(username + " не зарегистрирован в системе ДКП").then();
						}
					}
				}
			}
			return Mono.empty();
		});
	}


}
