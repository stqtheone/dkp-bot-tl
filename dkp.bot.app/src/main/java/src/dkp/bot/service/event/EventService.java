package src.dkp.bot.service.event;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import src.dkp.bot.entity.DkpEvent;
import src.dkp.bot.entity.Player;
import src.dkp.bot.repository.DkpEventRepository;
import src.dkp.bot.repository.PlayerRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService {

	private final DkpEventRepository dkpEventRepository;


	public void createDkpEvent(Message message) {
		Optional<User> userOptional = message.getAuthor();
		String[] content = message.getContent().trim().split(" ");
		if (content.length > 1) {
			if (userOptional.isPresent()) {
				DkpEvent dkpEvent = new DkpEvent();

				dkpEvent.setEventCode(content[0]);
				dkpEvent.setCreatedUser(userOptional.get().getUsername());
				dkpEvent.setDkpCount(Long.parseLong(content[1]));
				dkpEvent.setActive(true);

				dkpEventRepository.save(dkpEvent);

			}
		}

	}
}
