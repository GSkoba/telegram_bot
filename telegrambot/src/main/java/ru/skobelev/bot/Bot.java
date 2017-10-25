package ru.skobelev.bot;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot{

   @Override
    public void onUpdateReceived(Update e) {
        Calendar c = Calendar.getInstance();
        Date d = new Date();
        c.setTime(d);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        Message msg = e.getMessage(); // Это нам понадобится
        String txt = msg.getText();
        if (txt.equals("/start")) {
            sendMsg(msg, "Hello, world! This is simple bot!");
        }
        if(txt.equals("raz")){
            switch (dayOfWeek){
            case 2:
                sendMsg(msg,"Теор вер");
                sendMsg(msg,"ТАУ практика");
                sendMsg(msg,"ТАУ");
                sendMsg(msg,"Культурология");
                break;
            case 3:
                sendMsg(msg,"Нет занятий");
                break;
            case 4:
                sendMsg(msg,"Системы");
                sendMsg(msg,"САИУ");
                sendMsg(msg,"САИУ");
                break;
            case 5:
                sendMsg(msg,"Базы данных");
                break;
            case 6:
                sendMsg(msg,"Пятница");
                break;
            case 7:
                sendMsg(msg, "Нет пар");
                break;
            case 1:
                sendMsg(msg,"Нет пар");
                break;
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "BOT_NAME";
    }

    @Override
    public String getBotToken() {
        return "Token";
    }

    @SuppressWarnings("deprecation") // Означает то, что в новых версиях метод уберут или заменят
    private void sendMsg(Message msg, String text) {
        SendMessage s = new SendMessage();
        s.setChatId(msg.getChatId()); // Боту может писать не один человек, и поэтому чтобы отправить сообщение, грубо говоря нужно узнать куда его отправлять
        s.setText(text);
        try { //Чтобы не крашнулась программа при вылете Exception
            sendMessage(s);
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }
}
