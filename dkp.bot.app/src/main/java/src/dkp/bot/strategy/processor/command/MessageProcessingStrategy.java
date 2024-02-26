package src.dkp.bot.strategy.processor.command;

import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.core.publisher.Mono;

public interface MessageProcessingStrategy {
	boolean appliesTo(MessageCreateEvent event);

	Mono<Void> process(MessageCreateEvent event);
}