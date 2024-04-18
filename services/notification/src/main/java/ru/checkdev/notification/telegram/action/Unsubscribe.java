package ru.checkdev.notification.telegram.action;

import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.checkdev.notification.telegram.service.TgAuthCallWebClint;

import java.util.Objects;

/**
 * 3. Мидл
 * Класс реализует пункт меню отписки тг бота. Работа с бд не реализована
 *
 * @since 09.04.2024
 */
@AllArgsConstructor
public class Unsubscribe implements Action {

    private final TgAuthCallWebClint authCallWebClint;

    @Override
    public BotApiMethod<Message> handle(Message message) {
        var chatId = message.getChatId();
        var person = authCallWebClint.doGet("/person/telegram/" + chatId).block();
        assert person != null;
        if (!Objects.equals(person.getChatId(), chatId)) {
            return new SendMessage(chatId.toString(), "Id не соответствует");
        }
        return new SendMessage(chatId.toString(), "Успешно отписан");
    }

    @Override
    public BotApiMethod<Message> callback(Message message) {
        return handle(message);
    }
}