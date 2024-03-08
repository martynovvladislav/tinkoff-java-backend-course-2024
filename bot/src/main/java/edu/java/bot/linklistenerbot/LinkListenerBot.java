package edu.java.bot.linklistenerbot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.BaseResponse;
import edu.java.bot.commands.Command;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.processors.MessageProcessor;
import jakarta.annotation.PreDestroy;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LinkListenerBot implements Bot {
    private TelegramBot bot;
    private final ApplicationConfig applicationConfig;
    private final MessageProcessor messageProcessor;
    private final List<Command> commands;

    @Override
    public <T extends BaseRequest<T, R>, R extends BaseResponse> void execute(BaseRequest<T, R> request) {
        bot.execute(request);
    }

    @Override
    public int process(List<Update> update) {
        update.forEach(upd -> execute(messageProcessor.process(upd)));
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Override
    public void start() {
        this.bot = new TelegramBot(applicationConfig.telegramToken());
        bot.setUpdatesListener(this);
        SetMyCommands setMyCommands = new SetMyCommands(
            commands
                .stream()
                .map(Command::toApiCommand)
                .toArray(BotCommand[]::new)
        );
        bot.execute(setMyCommands);
    }

    @Override
    @PreDestroy
    public void close() {
        bot.removeGetUpdatesListener();
        bot.shutdown();
    }
}
