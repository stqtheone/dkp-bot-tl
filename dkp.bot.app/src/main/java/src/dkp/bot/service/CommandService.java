package src.dkp.bot.service;

import discord4j.core.event.domain.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import src.dkp.bot.listener.EventListener;
import src.dkp.bot.strategy.processor.command.MenuProcessingStrategy;

import java.util.List;

@Service
public class CommandService implements EventListener<Event> {
	@Autowired
	private List<MenuProcessingStrategy> strategies;

	@Override
	public Class<Event> getEventType() {
		return Event.class;
	}

	@Override
	public Mono<Void> execute(Event event) {
		return Flux.fromIterable(strategies)
				.filter(strategy -> strategy.appliesTo(event))
				.flatMap(strategy -> strategy.process(event))
				.then();
	}
}
