package src.dkp.bot.service.player;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import src.dkp.bot.entity.Player;
import src.dkp.bot.repository.PlayerRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlayerService {

	private final PlayerRepository playerRepository;


	public void createPlayer(Message message) {
		Optional<User> userOptional = message.getAuthor();
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			Player player = new Player();

			player.setDiscordName(user.getUsername());
			player.setDiscordId(user.getId().asString());
			player.setDkpCount(0L);
			player.setGameName(message.getContent());
			player.setActive(false);
			player.setConfirmed(false);

			playerRepository.save(player);

		}
	}

	public List<Player> getUnactivatedUsers() {
		return playerRepository.findAllByActiveFalseAndConfirmedFalse();
	}

	@Transactional
	public void addDkpCount(String nickname, Long count) {
		Optional<Player> player = playerRepository.findByGameName(nickname);

		if (player.isPresent()) {
			player.get().setDkpCount(player.get().getDkpCount() + count);
			playerRepository.save(player.get());
		}

	}

	@Transactional
	public void activateAllUnactiveUsers() {
		List<Player> players = getUnactivatedUsers();

		for (Player player: players) {
			player.setActive(true);
			player.setConfirmed(true);
			playerRepository.save(player);
		}
	}

	@Transactional
	public void activateUser(String nickname) {
		Player player = playerRepository.findByGameName(nickname).orElse(null);
		if (player != null) {
			player.setActive(true);
			player.setConfirmed(true);
			playerRepository.save(player);
		}
	}
}
