package src.dkp.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import src.dkp.bot.entity.DkpEvent;

public interface DkpEventRepository extends JpaRepository<DkpEvent, Long> {
}
