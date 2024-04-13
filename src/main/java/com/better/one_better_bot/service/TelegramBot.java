package com.better.one_better_bot.service;


import com.better.one_better_bot.config.BotConfig;
import com.better.one_better_bot.model.User;
import com.better.one_better_bot.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private UserRepository userRepository;
    final BotConfig config;

    static final String HELP_TEXT = "This bot is created to demonstrates Spring capabilities.\n\n" +
            "Ypu con execute commands from the main menu on the left or by typing a command:\n\n" +
            "Type /start to see a welcome message.\n\n"+
            "Type /mydata to see data stored about yourself\n\n"+
            "Type /deletedata удалить двнные \n\n"+
            "Type /settings хз";

    public TelegramBot(BotConfig config) {
        this.config = config;

        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "get a welcom message"));
        listOfCommands.add(new BotCommand("/mydata","get you data stored"));
        listOfCommands.add(new BotCommand("/deletedata", "delete my data"));
        listOfCommands.add(new BotCommand("/settings", "set you preferences settings"));
        listOfCommands.add(new BotCommand("/help", "info how to use this command"));
        try{
            this.execute(new SetMyCommands(listOfCommands,new BotCommandScopeDefault(),null));
        }
        catch (TelegramApiException e) {
            log.error("Error setting bot command list", e.getMessage());
        }
    }
    @Override
    public String getBotUsername() {
        return config.getBotName();
    }
    @Override
    public String getBotToken() {
        return config.getToken();
    }
    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            String massageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (massageText) {
                case "/start":
                    registerUser(update.getMessage());
                        startCommandReceived(chatId,update.getMessage().getChat().getFirstName());
                    break;
                case "/help":

                        sendMessage(chatId,HELP_TEXT);
                    break;
                    case "/deletedata":

                default: sendMessage(chatId,"Sorry, command not found");
            }
        }
    }

    private void registerUser(Message message) {
        if(userRepository.findById(message.getChatId()).isEmpty()){

            var chatId = message.getChatId();
            var chat = message.getChat();

            User user = new User();
            user.setChatId(chatId);
            user.setFirstName(chat.getFirstName());
            user.setLastName(chat.getLastName());
            user.setUserName(chat.getUserName());
            user.setJoinDate(new Timestamp(System.currentTimeMillis()));

            userRepository.save(user);
            log.info("user saved: "+ user);
        }
    }

    private void mydataComand(long chatId, String name){

    }
    private void startCommandReceived(long chatId, String name) {
        String answer = "Hi, " + name + " ,nice to meet you!";
        log.info("Replied to user " + name);


        sendMessage(chatId,answer);
    }
    private void sendMessage(long chatId, String textToSend){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        try{
            execute(message);
        }
        catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }
}
