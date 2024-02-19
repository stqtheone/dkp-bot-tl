package src.dkp.bot.entity;

import jakarta.persistence.*;
import jdk.jfr.Event;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class EventConfirmation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String screenshotUrl;

	private String screenshotBase64;

	@OneToOne
	private Player player;

	@OneToOne
	private DkpEvent event;

	@CreationTimestamp
	private LocalDateTime createdDate;
}
