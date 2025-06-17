package pro.sky.telegrambot.service;

import java.time.LocalDateTime;
import java.util.List;

import pro.sky.telegrambot.entity.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class NotificationScheduler {

    private final NotificationTaskRepository repository;
    private final TelegramService telegramService;

    public NotificationScheduler(NotificationTaskRepository repository, TelegramService telegramService) {
        this.repository = repository;
        this.telegramService = telegramService;
    }

    @Scheduled(cron = "0 * * * * *") 
    public void checkAndSendNotifications() {
        LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);
        LocalDateTime nextMinute = now.plusMinutes(1);

        List<NotificationTask> dueTasks = repository.findAllByNotificationDatetimeBetween(now, nextMinute);

        for (NotificationTask task : dueTasks) {
            telegramService.sendMessage(task.getChatId(), task.getMessageText());
            repository.delete(task);
        }
    }
}