package ru.checkdev.notification.telegram.action;

import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.checkdev.notification.telegram.service.TgAuthCallWebClint;

import java.util.Objects;

@AllArgsConstructor
public class CheckAction implements Action {

    private final TgAuthCallWebClint authCallWebClint;

    @Override
    public BotApiMethod<Message> handle(Message message) {
        var chatId = message.getChatId();
       var person =  authCallWebClint.doGet("/person/telegram/" + chatId);
       var email = Objects.requireNonNull(person.block()).getEmail();
       var username = Objects.requireNonNull(person.block()).getUsername();
       String text = "Ваш email - " + email + System.lineSeparator()
               + "Имя пользователя - " + username;
       return new SendMessage(message.getChatId().toString(), text);
    }

    @Override
    public BotApiMethod<Message> callback(Message message) {
        return handle(message);
    }
}
