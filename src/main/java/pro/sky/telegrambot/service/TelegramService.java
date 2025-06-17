package pro.sky.telegrambot.service;

import org.springframework.stereotype.Service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

@Service
public class TelegramService {

    private final TelegramBot telegramBot;

    public TelegramService(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage(chatId, text);
        try{
            SendResponse response = telegramBot.execute(message);
            
            if(!response.isOk()){
                System.err.println("Ошибка при отправке сообщения: " + response.description());
                telegramBot.execute(new SendMessage(chatId, "Ошибка при отправке сообщения: " + response.description()));
            }
        } catch (Exception e) {
            System.err.println("Ошибка при отправке сообщения: " + e.getMessage());
            telegramBot.execute(new SendMessage(chatId, "Ошибка при отправке сообщения: " + e.getMessage()));
        }
    }
}

