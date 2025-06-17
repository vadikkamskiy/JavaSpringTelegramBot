package pro.sky.telegrambot.entity;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification_task")
public class NotificationTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Column(name = "message_text", nullable = false)
    private String messageText;

    @Column(name = "notification_datetime", nullable = false)
    private LocalDateTime notificationDatetime;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public NotificationTask() {}

    public NotificationTask(Long chatId, String messageText, LocalDateTime notificationDatetime) {
        this.chatId = chatId;
        this.messageText = messageText;
        this.notificationDatetime = notificationDatetime;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public LocalDateTime getNotificationDatetime() {
        return notificationDatetime;
    }

    public void setNotificationDatetime(LocalDateTime notificationDatetime) {
        this.notificationDatetime = notificationDatetime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
