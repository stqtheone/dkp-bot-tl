package src.dkp.bot.strategy.processor.message;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import src.dkp.bot.handler.RegistrationEventStateHandler;
import src.dkp.bot.handler.UserInputStateHandler;
import src.dkp.bot.service.event.EventService;
import src.dkp.bot.service.player.PlayerService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegistrationEventMessageProcessor implements MessageProcessingStrategy {
	private final RegistrationEventStateHandler registrationEventStateHandler;
	private final EventService eventService;


	@Override
	public boolean appliesTo(MessageCreateEvent event) {
		Optional<User> user = event.getMessage().getAuthor();
		return user.filter(value -> registrationEventStateHandler.isAwaitingInput(value.getId().asString())).isPresent();

	}

	@Override
	public Mono<Void> process(Message message) {
			eventService.createDkpEvent(message);

			return message.getChannel()
					.flatMap(channel -> channel.createMessage("Событие создано")
							.then());


	}

}
