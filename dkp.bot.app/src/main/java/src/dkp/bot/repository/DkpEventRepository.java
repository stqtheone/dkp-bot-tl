package src.dkp.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import src.dkp.bot.entity.DkpEvent;

import java.util.Optional;

public interface DkpEventRepository extends JpaRepository<DkpEvent, Long> {

	Optional<DkpEvent> findByThreadId(String threadId);
}
