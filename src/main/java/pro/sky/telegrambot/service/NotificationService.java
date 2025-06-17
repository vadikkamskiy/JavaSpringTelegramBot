package pro.sky.telegrambot.service;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.entity.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class NotificationService {

    private final NotificationTaskRepository repository;

    public NotificationService(NotificationTaskRepository repository) {
        this.repository = repository;
    }

    public void handleIncomingMessage(Long chatId, String messageText) {
        Pattern pattern = Pattern.compile("^(\\d{2}\\.\\d{2}\\.\\d{4} \\d{2}:\\d{2})\\s+(.+)$");
        Matcher matcher = pattern.matcher(messageText);

        if (matcher.matches()) {
            String datetimeString = matcher.group(1);
            String text = matcher.group(2);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
            LocalDateTime dateTime = LocalDateTime.parse(datetimeString, formatter);
            LocalDateTime now = LocalDateTime.now();

            if (dateTime.isBefore(now)) {
                throw new IllegalArgumentException("❌ Нельзя создать напоминание в прошлом. Укажи время позже текущего.");
            }

            NotificationTask task = new NotificationTask(chatId, text, dateTime);
            repository.save(task);

            System.out.println("✅ Напоминание добавлено: " + text + " в " + dateTime);

        } else {
            throw new IllegalArgumentException("❌ Неверный формат. Пример: 12.12.2025 20:00 Сделать домашку");
        }
    }
}