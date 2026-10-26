package com.kursova.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // URL для підключення до бази даних course_management_db
    private static final String URL = "jdbc:mysql://localhost:3306/course_management_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

    // Стандартний логін та пароль для XAMPP/MySQL
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        try {
            // Реєстрація драйвера MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver не знайдено у Maven!", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }


    public static void main(String[] args) {
        System.out.println("Спроба підключення до БД...");

        try (Connection connection = getConnection()) {
            if (connection != null && !connection.isClosed()) {
                System.out.println("----------------------------------------");
                System.out.println("Підключено успішно до course_management_db!");
                System.out.println("----------------------------------------");
            }
        } catch (SQLException e) {
            System.err.println("----------------------------------------");
            System.err.println("Помилка підключення! Перевірте, чи запущено MySQL в XAMPP.");
            System.err.println("Деталі помилки: " + e.getMessage());
            System.err.println("----------------------------------------");
        }
    }
}
