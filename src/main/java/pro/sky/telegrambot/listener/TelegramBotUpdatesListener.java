package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;
import com.pengrad.telegrambot.request.SendMessage;

import pro.sky.telegrambot.entity.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;
    private NotificationTaskRepository notificationTaskRepository;
    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            // Process your updates here
            if (update.message() != null && update.message().text() != null) {
                String text = update.message().text();
                Long chatId = update.message().chat().id();
                if (text.equals("/start")) {
                    telegramBot.execute(new SendMessage(chatId, "Привет! Я бот для уведомлений о важных событиях."));
                } else {
                    telegramBot.execute(new SendMessage(chatId, "Я не понимаю эту команду. Попробуйте /start."));
                }

                Pattern pattern = Pattern.compile("^(\\d{2}\\.\\d{2}\\.\\d{4} \\d{2}:\\d{2})\\s+(.+)$");
                Matcher matcher = pattern.matcher(text);
                if (matcher.matches()) {
                try {
                    String dateTimeString = matcher.group(1);
                    String messageText = matcher.group(2);

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                    LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);

                    if (dateTime.isBefore(LocalDateTime.now())) {
                        telegramBot.execute(new SendMessage(chatId, "❌ Нельзя создать напоминание в прошлом."));
                        return;
                    }

                    NotificationTask task = new NotificationTask();
                    task.setChatId(chatId);
                    task.setMessageText(messageText);
                    task.setNotificationDatetime(dateTime);

                    notificationTaskRepository.save(task);

                    telegramBot.execute(new SendMessage(chatId, "✅ Напоминание успешно сохранено!"));
                } catch (Exception e) {
                    telegramBot.execute(new SendMessage(chatId, "⚠ Произошла ошибка при сохранении. Проверь формат: `дд.ММ.гггг чч:мм текст`"));
                }
            } else {
                telegramBot.execute(new SendMessage(chatId, "❓ Не понимаю. Пожалуйста, используй формат:\n`дд.ММ.гггг чч:мм Текст напоминания`"));
            }
            }   
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

}
