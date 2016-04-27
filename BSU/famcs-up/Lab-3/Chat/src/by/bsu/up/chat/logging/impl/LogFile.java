package by.bsu.up.chat.logging.impl;

import by.bsu.up.chat.logging.Logger;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogFile implements Logger {

    private static final String TEMPLATE = "[%s] %s";

    private String tag;

    private PrintWriter printWriter;

    private LogFile(Class<?> cls, String filename) {
        tag = String.format(TEMPLATE, cls.getName(), "%s");
        try {
            printWriter = new PrintWriter((new File(filename)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void info(String message) {
        printWriter.write(currentDate() + String.format(tag, message) + "\n");
        printWriter.flush();
    }

    @Override
    public void error(String message, Throwable e) {
        printWriter.write(currentDate() + String.format(tag, message) + e.getMessage() + "\n");
        printWriter.flush();
    }

    public String currentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        return simpleDateFormat.format(new Date());
    }

    public static LogFile create(Class<?> cls, String filename) {
        return new LogFile(cls, filename);
    }

}
