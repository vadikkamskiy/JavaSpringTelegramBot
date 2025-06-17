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

    public String handleIncomingMessage(Long chatId, String messageText) {
        Pattern pattern = Pattern.compile("^(\\d{2}\\.\\d{2}\\.\\d{4} \\d{2}:\\d{2})\\s+(.+)$");
        Matcher matcher = pattern.matcher(messageText);

        if (matcher.matches()) {
            try{
                String datetimeString = matcher.group(1);
                String text = matcher.group(2);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                LocalDateTime dateTime = LocalDateTime.parse(datetimeString, formatter);
                LocalDateTime now = LocalDateTime.now();

                if (dateTime.isBefore(now)) {
                    return "❌ Нельзя создать напоминание в прошлом. Укажи время позже текущего.";
                }

                NotificationTask task = new NotificationTask(chatId, text, dateTime);
                repository.save(task);

                return "✅ Напоминание добавлено: " + text + " в " + dateTime;

            } catch (Exception e) {
                return "❌ Ошибка при добавлении напоминания: " + e.getMessage();
            }
        }else{
            return "❓ Не понимаю. Пожалуйста, используй формат:\n`дд.ММ.гггг чч:мм Текст напоминания`";
        }
    }
}