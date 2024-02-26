package src.dkp.bot.strategy.processor.command.event.message;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.GuildChannel;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.core.object.entity.channel.ThreadChannel;
import discord4j.core.spec.StartThreadSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import src.dkp.bot.entity.DkpEvent;
import src.dkp.bot.handler.RegistrationEventStateHandler;
import src.dkp.bot.service.client.DiscordClientHelper;
import src.dkp.bot.service.event.EventService;
import src.dkp.bot.strategy.processor.command.MessageProcessingStrategy;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegistrationEventMessageProcessor implements MessageProcessingStrategy {
	private final RegistrationEventStateHandler registrationEventStateHandler;
	private final EventService eventService;
	private final String EVENTS_CHANNEL = "events";


	@Override
	public boolean appliesTo(MessageCreateEvent event) {
		Optional<User> user = event.getMessage().getAuthor();
		return user.filter(value -> registrationEventStateHandler.isAwaitingInput(value.getId().asString())).isPresent();

	}

	@Override
	public Mono<Void> process(MessageCreateEvent event) {
		Optional<User> user = event.getMessage().getAuthor();
		Mono<GuildChannel> channel = DiscordClientHelper.findChannelByName(event.getClient(), EVENTS_CHANNEL);
		DkpEvent dkpEvent = eventService.createDkpEvent(event.getMessage());

		if (dkpEvent != null) {
			registrationEventStateHandler.setAwaitingInput(user.get().getId().asString(), false);
			return channel.flatMap(ch -> {
				if (ch instanceof TextChannel textChannel) {
					return textChannel.createMessage(
							String.format("Предстоящий эвент: %s. Количество очков dkp: %s. Код: %s", dkpEvent.getDescription(), dkpEvent.getDkpCount(), dkpEvent.getEventCode())
					).flatMap(message -> {
						StartThreadSpec spec = StartThreadSpec.builder()
								.name("Скрины по данному событию подгружать сюда")
								.autoArchiveDuration(ThreadChannel.AutoArchiveDuration.of(60))
								.build();
						return message.startThread(spec).flatMap(thread -> {
									dkpEvent.setThreadId(thread.getId().asString());
									eventService.updateDkpEvent(dkpEvent);
									return Mono.empty();
								}
						).then();
					});
				}
				return Mono.empty();
			}).then(event.getMessage().getChannel().flatMap(ch ->
					ch.createMessage("Событие создано, анонс о событии будет отправлен в соответствующий канал")
			)).then();
		} else {
			return event.getMessage().getChannel().flatMap(ch ->
					ch.createMessage("Событие не создано. Ошибка в формате описания события. Повторите еще раз")
			).then();
		}


	}

}
