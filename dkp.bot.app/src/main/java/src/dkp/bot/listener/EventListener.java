package src.dkp.bot.listener;

import discord4j.core.event.domain.Event;
import reactor.core.publisher.Mono;

public interface EventListener <T extends Event> {

	Class<T> getEventType();

	Mono<Void> execute(T event);

	default Mono<Void> handleError(final Throwable error) {
		error.printStackTrace();

		return Mono.empty();
	}
}
