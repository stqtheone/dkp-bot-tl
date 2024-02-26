package src.dkp.bot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class DkpEvent {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String eventCode;

	@CreationTimestamp
	private LocalDateTime createdDate;

	private Long dkpCount;

	private String createdUser;

	private String description;

	@ManyToMany(mappedBy = "events")
	private List<Player> activeUsers;

	private Boolean active;

	private String threadId;

}
