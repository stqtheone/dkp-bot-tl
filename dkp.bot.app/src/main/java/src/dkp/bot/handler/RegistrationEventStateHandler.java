package src.dkp.bot.handler;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class RegistrationEventStateHandler {
	private final ConcurrentHashMap<String, Boolean> awaitingInput = new ConcurrentHashMap<>();

	public void setAwaitingInput(String userId, boolean isAwaiting) {
		awaitingInput.put(userId, isAwaiting);
	}

	public boolean isAwaitingInput(String userId) {
		return awaitingInput.getOrDefault(userId, false);
	}
}