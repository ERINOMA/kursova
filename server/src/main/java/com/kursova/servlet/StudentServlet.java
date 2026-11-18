package com.kursova.servlet;

import com.kursova.service.StudentService;
import com.kursova.util.LoggerUtil;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class StudentServlet extends HttpServlet {
    private final StudentService studentService = new StudentService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");

        try {
            String firstName = req.getParameter("firstName");
            String lastName = req.getParameter("lastName");
            String email = req.getParameter("email");
            String dateStr = req.getParameter("registrationDate");

            if (dateStr == null || dateStr.isBlank()) {
                throw new IllegalArgumentException("Дата реєстрації відсутня!");
            }
            LocalDate regDate = LocalDate.parse(dateStr.trim());

            studentService.registerStudent(firstName, lastName, email, regDate);

            resp.getWriter().write("{\"status\":\"success\"}");
        } catch (DateTimeParseException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"Неправильний формат дати (YYYY-MM-DD)\"}");
            LoggerUtil.error("HTTP POST Student: Помилка формату дати", e);
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
            LoggerUtil.error("HTTP POST Student: Помилка", e);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"Помилка збереження в БД\"}");
            LoggerUtil.error("HTTP POST Student: Помилка бази даних", e);
        }
    }
}
