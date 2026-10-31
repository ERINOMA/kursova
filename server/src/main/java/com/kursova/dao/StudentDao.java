package com.kursova.dao;

import com.kursova.config.DatabaseConnection;
import com.kursova.model.Student;
import com.kursova.util.LoggerUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDao {

    public void create(Student student) throws SQLException {
        String sql = "INSERT INTO students (first_name, last_name, email, registration_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, student.getFirstName());
            ps.setString(2, student.getLastName());
            ps.setString(3, student.getEmail());
            ps.setDate(4, Date.valueOf(student.getRegistrationDate()));

            ps.executeUpdate();
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    student.setId(generatedKeys.getInt(1));
                }
            }
            LoggerUtil.info("DAO: Успішно створено студента з ID: " + student.getId());
        } catch (SQLException e) {
            LoggerUtil.error("DAO: Помилка під час створення студента", e);
            throw e;
        }
    }

    public Student findById(int id) throws SQLException {
        String sql = "SELECT * FROM students WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Student s = new Student();
                    s.setId(rs.getInt("id"));
                    s.setFirstName(rs.getString("first_name"));
                    s.setLastName(rs.getString("last_name"));
                    s.setEmail(rs.getString("email"));
                    s.setRegistrationDate(rs.getDate("registration_date").toLocalDate());
                    return s;
                }
            }
        } catch (SQLException e) {
            LoggerUtil.error("DAO: Помилка під час пошуку студента за ID: " + id, e);
            throw e;
        }
        return null;
    }
}