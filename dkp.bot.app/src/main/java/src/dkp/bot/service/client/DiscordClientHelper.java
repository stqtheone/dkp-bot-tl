package src.dkp.bot.service.client;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ModalSubmitInteractionEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.channel.GuildChannel;
import discord4j.discordjson.json.ComponentData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscordClientHelper {

	private static final String GUILD_NAME_TEST = "Сервер rat";
	private static final String GUILD_NAME_MAIN = "JustPlay";


	public static Mono<Guild> getGuild(GatewayDiscordClient discordClient) {
		return discordClient.getGuilds()
				.filter(guild -> guild.getName().equalsIgnoreCase(GUILD_NAME_MAIN) || guild.getName().equalsIgnoreCase(GUILD_NAME_TEST))
				.next();
	}

	public static Mono<GuildChannel> findChannelByName(GatewayDiscordClient discordClient, String channelName) {
		Mono<Guild> guildMono = getGuild(discordClient);
		return guildMono.flatMapMany(Guild::getChannels)
				.filter(channel -> channel.getName().equalsIgnoreCase(channelName))
				.ofType(GuildChannel.class)
				.next();
	}


	public static String getValueFromModalComponent(ModalSubmitInteractionEvent modalSubmitInteractionEvent, String customId) {
		List<ComponentData> inputs = modalSubmitInteractionEvent.getInteraction()
				.getData().data().get().components().get();

		return inputs.stream()
				.map(cd -> cd.components().get().get(0))
				.filter(cd -> cd.customId().get().equals(customId))
				.map(cd -> cd.value().get())
				.findFirst().orElse(null);
	}
}
