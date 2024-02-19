package src.dkp.bot.handler;
import org.springframework.stereotype.Component;
import src.dkp.bot.dto.ConfirmationData;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class ConfirmationInputStateHandler {
	private final ConcurrentHashMap<String, String> awaitingConfirmation = new ConcurrentHashMap<>();

	public void setAwaitingInput(String userId, String data) {
		awaitingConfirmation.put(userId, data);
	}

	public String getData(String userId) {
		return awaitingConfirmation.getOrDefault(userId, null);
	}

	public void deleteData(String userId) {
		awaitingConfirmation.remove(userId);
	}


}