package com.kursova.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggerUtil {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public static void info(String message) {
        System.out.println(String.format("[%s] [INFO] %s",
                LocalDateTime.now().format(formatter), message));
    }

    public static void error(String message, Throwable throwable) {
        System.err.println(String.format("[%s] [ERROR] %s. Деталі помилки: %s",
                LocalDateTime.now().format(formatter), message, throwable.getMessage()));
    }
}
