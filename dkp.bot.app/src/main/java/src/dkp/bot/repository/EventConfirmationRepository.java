package src.dkp.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import src.dkp.bot.entity.DkpEvent;
import src.dkp.bot.entity.EventConfirmation;

public interface EventConfirmationRepository extends JpaRepository<EventConfirmation, Long> {
}
