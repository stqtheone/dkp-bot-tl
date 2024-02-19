package src.dkp.bot.strategy.processor.message;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import src.dkp.bot.handler.UserInputStateHandler;
import src.dkp.bot.service.player.PlayerService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlayerNicknameMessageProcessor implements MessageProcessingStrategy {
	private final UserInputStateHandler userInputStateHandler;
	private final PlayerService playerService;

	private final String DKP_REGISTRATION = "dkp_registration";

	@Override
	public boolean appliesTo(MessageCreateEvent event) {
		Optional<User> user = event.getMessage().getAuthor();
		return user.filter(value -> userInputStateHandler.isAwaitingInput(value.getId().asString())).isPresent();

	}

	@Override
	public Mono<Void> process(Message message) {
		Optional<User> user = message.getAuthor();
		if (user.isPresent()) {
			playerService.createPlayer(message);
			return message.getChannel()
					.flatMap(channel -> channel.createMessage("Заявка на регистрацию в системе ДКП отправлена администраторам сервера. Пожалуйста, ожидайте регистрации")
							.then());
		}
		return Mono.empty();

	}

}
