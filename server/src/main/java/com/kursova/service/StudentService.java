package com.kursova.service;

import com.kursova.dao.StudentDao;
import com.kursova.model.Student;
import com.kursova.util.LoggerUtil;

import java.sql.SQLException;
import java.time.LocalDate;

public class StudentService {
    private final StudentDao studentDao;

    public StudentService() {
        this.studentDao = new StudentDao();
    }

    public StudentService(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    public void registerStudent(String firstName, String lastName, String email, LocalDate regDate) throws SQLException {
        validateStudentData(firstName, lastName, email, regDate);

        Student student = new Student();
        student.setFirstName(firstName.trim());
        student.setLastName(lastName.trim());
        student.setEmail(email.trim());
        student.setRegistrationDate(regDate);

        studentDao.create(student);
        LoggerUtil.info("Студента успішно додано " + email);
    }

    public void validateStudentData(String firstName, String lastName, String email, LocalDate regDate) {
        if (firstName == null || firstName.isBlank() || lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Помилка: Ім'я та прізвище порожні!");
        }
        if (email == null || !email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("Помилка: Некоректний формат email (" + email + ")");
        }
        if (regDate == null) {
            throw new IllegalArgumentException("Помилка: Дата реєстрації порожня!");
        }
    }
}
