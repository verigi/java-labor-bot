package ru.zavorykin;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.zavorykin.services.KeyboardService;
import ru.zavorykin.services.LogService;

import java.util.List;

@Data
@Component
public class MyBot extends TelegramLongPollingBot {
    private final String BOT_NAME;
    private final String BOT_TOKEN;
    private final KeyboardService KEYBOARD_SERVICE;
    private static LogService logService = new LogService();

    @Autowired
    public MyBot(BotProperties botProperties,
                 KeyboardService keyboardService) {
        this.BOT_NAME = botProperties.getName();
        this.BOT_TOKEN = botProperties.getToken();
        this.KEYBOARD_SERVICE = keyboardService;
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            handleTextCommand(update.getMessage());
        } else if (update.hasCallbackQuery()) {
            handleCallback(update.getCallbackQuery());
        }
    }


    @Override
    public void onRegister() {
        List<BotCommand> commands = List.of(new BotCommand("start", "Запустить/перезапустить бота"));

        SetMyCommands setMyCommands = new SetMyCommands(commands, new BotCommandScopeDefault(), null);
        try {
            this.execute(setMyCommands);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void handleTextCommand(Message message) {
        String userText = message.getText();
        Long chatId = message.getChatId();

        SendMessage response;
        switch (userText.toLowerCase()) {
            case "/start" -> response = KEYBOARD_SERVICE.createMessageWithKeyboard(chatId, """
                    🤖 *Привет!*
                    Добро пожаловать в нашего бота!
                                        
                    Вы можете воспользоваться меню ниже.
                    """, KEYBOARD_SERVICE.getMainMenuInlineKeyboard());
            default -> response = new SendMessage(chatId.toString(), "Я пока не понимаю эту команду.");
        }

        try {
            execute(response);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void handleCallback(CallbackQuery callbackQuery) {
        String callbackData = callbackQuery.getData();
        Long chatId = callbackQuery.getMessage().getChatId();

        SendMessage message;
        switch (callbackData) {
            case "CMD_START" ->
                    message = KEYBOARD_SERVICE.createMessageWithKeyboard(chatId, "Главное меню: ", KEYBOARD_SERVICE.getMainMenuInlineKeyboard());
            case "CMD_APPOINTMENT" ->
                    message = KEYBOARD_SERVICE.createMessageWithKeyboard(chatId, "Меню записи: ", KEYBOARD_SERVICE.getAppointmentMenuInlineKeyboard());
            case "CMD_HELP" ->
                    message = KEYBOARD_SERVICE.createMessageWithKeyboard(chatId, "Раздел помощи: ", KEYBOARD_SERVICE.getHelpMenuInlineKeyboard());
            case "CMD_FAQ" ->
                    message = KEYBOARD_SERVICE.createMessageWithKeyboard(chatId, "Здесь будет раздел с частыми вопросами", KEYBOARD_SERVICE.getHelpMenuInlineKeyboard());
            case "CMD_BACK" ->
                    message = KEYBOARD_SERVICE.createMessageWithKeyboard(chatId, "Возврат в главное меню", KEYBOARD_SERVICE.getMainMenuInlineKeyboard());
            default -> message = new SendMessage(chatId.toString(), "Пока неизвестная команда");
        }

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}