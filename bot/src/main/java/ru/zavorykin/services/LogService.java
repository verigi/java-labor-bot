package ru.zavorykin.services;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class LogService {
    public void log(Update update) {
        String username = update.getMessage().getAuthorSignature() != null ? update.getMessage().getAuthorSignature() : "";
        System.out.println("--------------------------------");
        System.out.println("username: " + username);
        System.out.println("message: " + update.getMessage());
        System.out.println("--------------------------------");
    }
}