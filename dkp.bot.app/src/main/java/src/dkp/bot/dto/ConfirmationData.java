package src.dkp.bot.dto;

import lombok.Data;
import org.springframework.boot.convert.DataSizeUnit;

@Data
public class ConfirmationData {

	private String userId;

	private String value;
}
