package ru.skobelev.bot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.jsoup.nodes.Document;


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
        /*if(txt.equals("raz")){
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
        }*/
        if(txt.equals("raz")) {

            List<Lesson> lessons = new ArrayList<>();
            List<String> date = new ArrayList<>();
            try {
                Document document = Jsoup.connect("http://ruz.spbstu.ru/faculty/95/groups/24133").get();

                Elements liElements = document.getElementsByAttributeValue("class", "schedule__day");

                liElements.forEach(element -> {
                    Element divElement = element.child(0);
                    String data = divElement.text();
                    Element spanElement = element.child(1).child(0).child(0).child(2);
                    String text = spanElement.text();
                    spanElement = element.child(1).child(0).child(0).child(0).child(0);
                    String startTime = spanElement.text();
                    spanElement = element.child(1).child(0).child(0).child(0).child(2);
                    String endTime = spanElement.text();
                    spanElement = element.child(1).child(0).child(1).child(0);
                    String type = spanElement.text();
                    Elements tichersInThis = element.getElementsByAttributeValue("class","lesson__teachers");
                  //  spanElement = spanElement.text();
                    String teacher = "a";// tichersInThis.last().child(0).child(0).child(2).text();
                   // spanElement = element.child(1).child(0).child(1).child(2).child(0).child(0).child(0).child(0);
                    String where = "w";//spanElement.text();
                    lessons.add(new Lesson(data,text,startTime,endTime,type,teacher,where));
                    //ate.add(data);
                });


            } catch (IOException ex) {
                sendMsg(msg,ex.toString());
            }

            for (Lesson v: lessons
            ) {
                sendMsg(msg,v.sendDate());
            }

        }
    }

    @Override
    public String getBotUsername() {
        return "bot";
    }

    @Override
    public String getBotToken() {
        return "token";
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


class Lesson{
    String timeStart;
    String timeEnd;
    String nameLesson;
    String teacher;
    String date;
    String typeOfLesson;
    String placeWLesson;

    public Lesson(String date, String text, String timeStart, String timeEnd, String typeOfLesson, String teacher, String placeWLesson){
        this.placeWLesson = placeWLesson;
        this.typeOfLesson = typeOfLesson;
        this.teacher = teacher;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.nameLesson = text;
        this.date = date;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }
    public Lesson(String nameLesson, String teacher, String date, String typeOfLesson, String placeWLesson) {
        this.nameLesson = nameLesson;
        this.teacher = teacher;
        this.date = date;
        this.typeOfLesson = typeOfLesson;
        this.placeWLesson = placeWLesson;
    }



    public String getNameLesson() {
        return nameLesson;
    }

    public void setNameLesson(String nameLesson) {
        this.nameLesson = nameLesson;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTypeOfLesson() {
        return typeOfLesson;
    }

    public void setTypeOfLesson(String typeOfLesson) {
        this.typeOfLesson = typeOfLesson;
    }

    public String getPlaceWLesson() {
        return placeWLesson;
    }

    public void setPlaceWLesson(String placeWLesson) {
        this.placeWLesson = placeWLesson;
    }


    public String sendDate(){
        return  date +"\n"+timeStart + "-"+timeEnd + "\n"+nameLesson + " ("+typeOfLesson+") "+"\n"+teacher+"\n" + placeWLesson;
    }
}
