package ru.checkdev.notification.telegram.action;

import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.checkdev.notification.telegram.config.TgConfig;
import ru.checkdev.notification.telegram.service.TgAuthCallWebClint;


@AllArgsConstructor
public class ForgetAction implements Action {

    private final TgAuthCallWebClint tgAuthCallWebClint;
    private final TgConfig tgConfig = new TgConfig("tg/", 8);

    @Override
    public BotApiMethod<Message> handle(Message message) {
        var chatId = message.getChatId();
        var personUpdate = tgAuthCallWebClint.doPut("/person/updatePasswordTg", chatId).block();
        if (personUpdate != null) {
            String text = "Ваш пароль - " + tgConfig.getObjectToMap(personUpdate).get("password");
            return new SendMessage(message.getChatId().toString(), text);
        }
        return new SendMessage(message.getChatId().toString(), "Не удалось восстановить");
    }

    @Override
    public BotApiMethod<Message> callback(Message message) {
        return handle(message);
    }
}