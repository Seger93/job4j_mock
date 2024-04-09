package ru.checkdev.notification.telegram.action;

import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.checkdev.notification.telegram.config.TgConfig;
import ru.checkdev.notification.telegram.service.TgAuthCallWebClint;

import java.util.Objects;

/**
 * 3. Мидл
 * Класс реализует пункт меню подписки тг бота. Работа с бд не реализована
 *
 * @since 09.04.2024
 */
@AllArgsConstructor
public class Subscribe implements Action {

    private final TgAuthCallWebClint authCallWebClint;

    private final TgConfig tgConfig = new TgConfig("tg/", 8);

    @Override
    public BotApiMethod<Message> handle(Message message) {
        var chatId = message.getChatId().toString();
        var text = "Для подписки введите email";
        return new SendMessage(chatId, text);
    }

    @Override
    public BotApiMethod<Message> callback(Message message) {
        var chatId = message.getChatId().toString();
        var inputMess = message.getText();
        var text = "";
        var sl = System.lineSeparator();

        if (!tgConfig.isEmail(inputMess)) {
            text = "Email: " + inputMess + " не корректный." + sl
                    + "попробуйте снова." + sl
                    + "/subscribe";
            return new SendMessage(chatId, text);
        }
        var person = authCallWebClint.doGet("/person/telegram/" + chatId);
        if (!Objects.requireNonNull(person.block()).getEmail().equals(inputMess)) {
            return new SendMessage(message.getChatId().toString(), "Почта не принадлежит вашему Id");
        }
        return new SendMessage(message.getChatId().toString(), "Успешно подписан");
    }
}