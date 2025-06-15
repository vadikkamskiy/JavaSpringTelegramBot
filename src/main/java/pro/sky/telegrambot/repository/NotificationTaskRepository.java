package pro.sky.telegrambot.repository;

import pro.sky.telegrambot.entity.NotificationTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationTaskRepository extends JpaRepository<NotificationTask, Long> {

    List<NotificationTask> findByNotificationDatetime(LocalDateTime dateTime);

    List<NotificationTask> findByChatId(Long chatId);

    List<NotificationTask> findByNotificationDatetimeBefore(LocalDateTime now);
}
