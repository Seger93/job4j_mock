package ru.checkdev.notification.telegram.action;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

public class UnknownAction implements Action {

    private final List<String> actions;

    public UnknownAction(List<String> actions) {
        this.actions = actions;
    }

    @Override
    public BotApiMethod<Message> handle(Message message) {
        var chatId = message.getChatId().toString();
        String text = "Такой команды не существует - ";
        String sl = System.lineSeparator();
        var out = new StringBuilder();
        out.append("Выберите действие:").append(sl);
        for (String action : actions) {
            out.append(action).append(sl);
        }
        return new SendMessage(chatId, text + out);
    }

    @Override
    public BotApiMethod<Message> callback(Message message) {
        return handle(message);
    }
}