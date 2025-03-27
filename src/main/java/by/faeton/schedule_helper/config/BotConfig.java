package by.faeton.schedule_helper.config;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "bot")
public record BotConfig(

    String botName,
    String botToken,
    String sheetId,
    String apiKey,
    String sheetName,
    String range

) {
}
