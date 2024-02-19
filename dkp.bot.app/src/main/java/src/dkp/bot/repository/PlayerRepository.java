package src.dkp.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import src.dkp.bot.entity.Player;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
	Optional<Player> findByGameName(String gameName);

	List<Player> findAllByActiveFalseAndConfirmedFalse();
}