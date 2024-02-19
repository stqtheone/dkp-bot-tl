package src.dkp.bot.service;

import discord4j.core.event.domain.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import src.dkp.bot.listener.EventListener;
import src.dkp.bot.strategy.processor.message.MessageProcessingStrategy;

import java.util.List;

@Service
public class MessageCreateService implements EventListener<MessageCreateEvent> {
	@Autowired
	private List<MessageProcessingStrategy> strategies;

	@Override
	public Class<MessageCreateEvent> getEventType() {
		return MessageCreateEvent.class;
	}

	@Override
	public Mono<Void> execute(MessageCreateEvent event) {
		return Flux.fromIterable(strategies)
				.filter(strategy -> strategy.appliesTo(event))
				.flatMap(strategy -> strategy.process(event.getMessage()))
				.then();
	}
}
