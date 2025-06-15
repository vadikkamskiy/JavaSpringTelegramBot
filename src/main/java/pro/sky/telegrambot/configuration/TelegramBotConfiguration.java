package pro.sky.telegrambot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.DeleteMyCommands;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramBotConfiguration {

    private final Dotenv dotenv = Dotenv.configure().load();

    @Bean
    public TelegramBot telegramBot() {
        String token = dotenv.get("BOT_TOKEN");

        if (token == null || token.isBlank()) {
            throw new IllegalStateException("BOT_TOKEN is not set in .env file");
        }

        TelegramBot bot = new TelegramBot(token);

        bot.execute(new DeleteMyCommands());

        return bot;
    }
}
