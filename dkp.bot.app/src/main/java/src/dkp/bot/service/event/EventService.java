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


	public DkpEvent createDkpEvent(Message message) {
		Optional<User> userOptional = message.getAuthor();
		String[] content = message.getContent().trim().split(":");
		if (content.length > 2) {
			if (userOptional.isPresent()) {
				DkpEvent dkpEvent = new DkpEvent();

				dkpEvent.setEventCode(content[0]);
				dkpEvent.setCreatedUser(userOptional.get().getUsername());
				dkpEvent.setDkpCount(Long.parseLong(content[1]));
				dkpEvent.setDescription(content[2]);
				dkpEvent.setActive(true);

				dkpEventRepository.save(dkpEvent);
				return dkpEvent;

			}
		}
		return null;

	}

	public void updateDkpEvent(DkpEvent dkpEvent) {
		dkpEventRepository.save(dkpEvent);
	}

	public DkpEvent findDkpEventByThreadId(String threadId) {
		return dkpEventRepository.findByThreadId(threadId).orElse(null);
	}
}
