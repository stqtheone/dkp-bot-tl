package src.dkp.bot.strategy.processor.command;

import discord4j.core.event.domain.Event;
import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.core.event.domain.interaction.SelectMenuInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import discord4j.core.object.component.SelectMenu;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import src.dkp.bot.entity.Player;
import src.dkp.bot.handler.ConfirmationInputStateHandler;
import src.dkp.bot.service.player.PlayerService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConfirmationMessageProcessor implements MenuProcessingStrategy {
	private final PlayerService playerService;
	private final ConfirmationInputStateHandler confirmationInputStateHandler;

	private final String CONFIRM_USER = "confirm_user";
	private final String CONFIRM_ALL_USERS = "confirm_all_users";
	private final String USER_CONFIRMATION = "user_confirmation";


	@Override
	public boolean appliesTo(Event event) {
		if (event instanceof SelectMenuInteractionEvent selectMenuInteractionEvent) {
			String selectCode = selectMenuInteractionEvent.getValues().get(0);
			return selectCode.contains(":confirmation");
		} else if (event instanceof ButtonInteractionEvent buttonInteractionEvent) {
			return buttonInteractionEvent.getCustomId().equals(CONFIRM_USER)
					|| buttonInteractionEvent.getCustomId().equals(CONFIRM_ALL_USERS)
					|| buttonInteractionEvent.getCustomId().equals(USER_CONFIRMATION);
		}
		return false;

	}

	@Override
	public Mono<Void> process(Event event) {
		List<SelectMenu.Option> options = new ArrayList<>();

		List<Player> players = playerService.getUnactivatedUsers();

		for (Player player : players) {
			options.add(SelectMenu.Option.of(player.getGameName(), player.getGameName() + ":confirmation"));
		}

		SelectMenu selectMenu = SelectMenu.of("select_user", options).withPlaceholder("Выберите пользователя");

		Button confirm = Button.primary("confirm_user", "Подтвердить");
		Button confirmAllUsers = Button.primary("confirm_all_users", "Подтвердить всех из списка");
		Button returnToDkp = Button.primary("back_to_dkp", "Назад");
		Button returnToMenu = Button.primary("back_to_menu", "Главное меню");


		if (event instanceof SelectMenuInteractionEvent selectMenuInteractionEvent) {
			String selectedValue = selectMenuInteractionEvent.getValues().get(0).split(":")[0];
			String userId = selectMenuInteractionEvent.getInteraction().getUser().getId().asString();

			confirmationInputStateHandler.setAwaitingInput(userId, selectedValue);

			return selectMenuInteractionEvent.deferEdit();

		} else if (event instanceof ButtonInteractionEvent buttonInteractionEvent) {
			if (buttonInteractionEvent.getCustomId().equals(USER_CONFIRMATION)) {
				if (players.isEmpty()) {
					return (buttonInteractionEvent.edit()
							.withComponents(
									ActionRow.of(returnToDkp, returnToMenu)
							)
							.withContent("Нет доступных пользователей для регистрации")
							.withEphemeral(true));

				}
				return (buttonInteractionEvent.edit()
						.withComponents(
								ActionRow.of(selectMenu),
								ActionRow.of(confirm, confirmAllUsers, returnToDkp, returnToMenu)
						)
						.withEphemeral(true));
			} else {
				String userId = buttonInteractionEvent.getInteraction().getUser().getId().asString();
				String command = buttonInteractionEvent.getCustomId();

				switch (command) {
					case "confirm_user" -> {
						playerService.activateUser(confirmationInputStateHandler.getData(userId));
						confirmationInputStateHandler.deleteData(userId);
						players = playerService.getUnactivatedUsers();
						options = new ArrayList<>();
						if (players.isEmpty()) {
							return (buttonInteractionEvent.edit()
									.withComponents(
											ActionRow.of(confirm, confirmAllUsers, returnToDkp, returnToMenu)
									)
									.withContent("Пользователь успешно зарегистрирован. Более Нет доступных пользователей для регистрации")
									.withEphemeral(true));
						}
						for (Player player : players) {
							options.add(SelectMenu.Option.of(player.getGameName(), player.getGameName() + ":confirmation"));
						}
						selectMenu = SelectMenu.of("select_user", options).withPlaceholder("Выберите пользователя");
						return (buttonInteractionEvent.edit()
								.withComponents(
										ActionRow.of(selectMenu),
										ActionRow.of(confirm, confirmAllUsers, returnToDkp, returnToMenu)
								)
								.withContent("Пользователь успешно зарегистрирован в системе")
								.withEphemeral(true));
					}
					case "confirm_all_users" -> {
						playerService.activateAllUnactiveUsers();
						confirmationInputStateHandler.deleteData(userId);
						return (buttonInteractionEvent.edit()
								.withComponents(
										ActionRow.of(returnToDkp, returnToMenu)
								)
								.withContent("Пользователи успешно зарегистрированы в системе")
								.withEphemeral(true));
					}
				}
			}

		}
		return Mono.empty();
	}

}


