package src.dkp.bot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Player {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String discordName;

	private String discordId;

	private String gameName;

	private Long dkpCount;

	private Boolean active;

	private Boolean confirmed;

	@ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	@JoinTable(
			name = "user_event",
			joinColumns = { @JoinColumn(name = "user_id") },
			inverseJoinColumns = { @JoinColumn(name = "event_id") }
	)
	private List<DkpEvent> events = new ArrayList<>();

	@CreationTimestamp
	private LocalDateTime createdDate;
}
