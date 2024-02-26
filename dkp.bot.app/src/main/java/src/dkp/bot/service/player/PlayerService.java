package src.dkp.bot.service.player;

import discord4j.core.object.entity.User;
import lombok.RequiredArgsConstructor;
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


	public void createPlayer(String nickname, String discordId, String username) {

		Player player = new Player();

		player.setDiscordName(username);
		player.setDiscordId(discordId);
		player.setDkpCount(0L);
		player.setGameName(nickname);
		player.setActive(false);
		player.setConfirmed(false);

		playerRepository.save(player);

	}

	@Transactional
	public Player getPlayerByDiscordId(String discordId) {
		return playerRepository.findByActiveTrueAndConfirmedTrueAndDiscordId(discordId).orElse(null);
	}


	public void updatePlayer(Player player) {
		playerRepository.save(player);
	}

	public List<Player> getUnactivatedUsers() {
		return playerRepository.findAllByActiveFalseAndConfirmedFalse();
	}

	public List<Player> getAllActiveAndConfirmedPlayers() {
		return playerRepository.findAllByActiveTrueAndConfirmedTrue();
	}

	@Transactional
	public Optional<Player> addDkpCount(String nickname, Long count) {
		Optional<Player> player = playerRepository.findByGameName(nickname);

		if (player.isPresent()) {
			player.get().setDkpCount(player.get().getDkpCount() + count);
			playerRepository.save(player.get());
		}

		return player;

	}

	@Transactional
	public void activateAllUnactiveUsers() {
		List<Player> players = getUnactivatedUsers();

		for (Player player : players) {
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
