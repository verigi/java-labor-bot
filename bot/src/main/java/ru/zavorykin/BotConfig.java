package ru.zavorykin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.zavorykin.services.KeyboardService;

@Configuration
public class BotConfig {
    @Bean
    public MyBot myBot(BotProperties botProperties, KeyboardService keyboardService) {
        return new MyBot(botProperties, keyboardService);
    }

    @Bean
    public TelegramBotsApi telegramBotsApi(MyBot myBot) throws TelegramApiException {
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(myBot);
        return api;
    }
}