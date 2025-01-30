package ru.zavorykin.services;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;

@Service
public class KeyboardService {

    public InlineKeyboardMarkup createInlineKeyboard(Map<String, String> buttons) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (Map.Entry<String, String> entry : buttons.entrySet()) {
            InlineKeyboardButton button = new InlineKeyboardButton(entry.getKey());
            button.setCallbackData(entry.getValue());
            rows.add(Collections.singletonList(button));
        }

        markup.setKeyboard(rows);
        return markup;
    }

    public InlineKeyboardMarkup getMainMenuInlineKeyboard() {
        LinkedHashMap buttons = new LinkedHashMap();

        buttons.put("📝 Запись к юристу", "CMD_APPOINTMENT");
        buttons.put("📄 Подготовить шаблон документов", "CMD_DOC");
        buttons.put("❓ Помощь", "CMD_HELP");
        buttons.put("🏠 О нас", "CMD_ABOUT");

        return createInlineKeyboard(buttons);
    }

    public InlineKeyboardMarkup getAppointmentMenuInlineKeyboard() {
        LinkedHashMap buttons = new LinkedHashMap();

        buttons.put("📅 Выбрать дату", "CMD_DATE");
        buttons.put("🕒 Выбрать время", "CMD_TIME");
        buttons.put("📞 Оставить контакт", "CMD_CONTACT");
        buttons.put("✅ Подтвердить запись", "CMD_CONFIRM");
        buttons.put("⬅️ Назад", "CMD_BACK");

        return createInlineKeyboard(buttons);
    }

    public InlineKeyboardMarkup getHelpMenuInlineKeyboard() {
        LinkedHashMap buttons = new LinkedHashMap();

        buttons.put("❓ FAQ", "CMD_FAQ");
        buttons.put("⬅️ Назад", "CMD_BACK");

        return createInlineKeyboard(buttons);
    }

    public SendMessage createMessageWithKeyboard(Long chatId, String text, InlineKeyboardMarkup inlineKeyboardMarkup) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        message.setParseMode("Markdown");
        message.setReplyMarkup(inlineKeyboardMarkup);

        return message;
    }
}