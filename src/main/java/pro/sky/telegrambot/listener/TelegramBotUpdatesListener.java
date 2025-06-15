package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;

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
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

}
