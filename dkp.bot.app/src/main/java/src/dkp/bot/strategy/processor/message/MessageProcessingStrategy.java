package src.dkp.bot.strategy.processor.message;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import jdk.jfr.Event;
import reactor.core.publisher.Mono;

public interface MessageProcessingStrategy {
	boolean appliesTo(MessageCreateEvent event);

	Mono<Void> process(Message message);
}