package src.dkp.bot.listener;

import discord4j.core.object.entity.Message;
import reactor.core.publisher.Mono;

public abstract class MessageListener {


	private String author = "UNKNOWN";

	public Mono<Void> processMessage(final Message eventMessage) {
		return Mono.just(eventMessage)
				.filter(message -> {
					final Boolean isNotBot = eventMessage.getAuthor()
							.map(user -> !user.isBot())
							.orElse(false);

					if (isNotBot) {
						eventMessage.getAuthor().ifPresent(user -> author = user.getUsername());
					}
					return isNotBot;
				})
				.flatMap(Message::getChannel)
				.flatMap(channel -> channel.createMessage(String.format("Hello '%s'", author)))
				.then();

	}
}
