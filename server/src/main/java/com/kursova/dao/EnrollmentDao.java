package com.kursova.dao;

import com.kursova.config.DatabaseConnection;
import com.kursova.model.Enrollment;
import com.kursova.model.EnrollmentStatus;
import com.kursova.util.LoggerUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDao {

    // Створення запису (C - Create)
    public void create(Enrollment enrollment) throws SQLException {
        String sql = "INSERT INTO enrollments (course_id, student_id, status, factor_f1, factor_f2, factor_f3) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, enrollment.getCourseId());
            ps.setInt(2, enrollment.getStudentId());
            ps.setString(3, enrollment.getStatus().name());
            ps.setInt(4, enrollment.getFactorF1());
            ps.setInt(5, enrollment.getFactorF2());
            ps.setInt(6, enrollment.getFactorF3());

            ps.executeUpdate();

            // Отримуємо автоматично згенерований базою даних ID для об'єкта
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    enrollment.setId(generatedKeys.getInt(1));
                }
            }
            LoggerUtil.info("DAO: Успішно створено зарахування з ID: " + enrollment.getId());
        } catch (SQLException e) {
            LoggerUtil.error("DAO: Помилка під час створення зарахування", e);
            throw e;
        }
    }

    // Читання запису за ID (R - Read)
    public Enrollment findById(int id) throws SQLException {
        String sql = "SELECT * FROM enrollments WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            LoggerUtil.error("DAO: Помилка під час пошуку зарахування за ID: " + id, e);
            throw e;
        }
        return null;
    }

    // Читання повного списку записів (R - Read All)
    public List<Enrollment> findAll() throws SQLException {
        List<Enrollment> list = new ArrayList<>();
        String sql = "SELECT * FROM enrollments";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            LoggerUtil.error("DAO: Помилка під час отримання списку зарахувань", e);
            throw e;
        }
        return list;
    }

    // Редагування запису (U - Update)
    public void update(Enrollment enrollment) throws SQLException {
        String sql = "UPDATE enrollments SET status = ?, factor_f1 = ?, factor_f2 = ?, factor_f3 = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, enrollment.getStatus().name());
            ps.setInt(2, enrollment.getFactorF1());
            ps.setInt(3, enrollment.getFactorF2());
            ps.setInt(4, enrollment.getFactorF3());
            ps.setInt(5, enrollment.getId());

            ps.executeUpdate();
            LoggerUtil.info("DAO: Успішно оновлено зарахування з ID: " + enrollment.getId());
        } catch (SQLException e) {
            LoggerUtil.error("DAO: Помилка під час оновлення зарахування з ID: " + enrollment.getId(), e);
            throw e;
        }
    }

    // Видалення запису (D - Delete)
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM enrollments WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            LoggerUtil.info("DAO: Успішно видалено зарахування з ID: " + id);
        } catch (SQLException e) {
            LoggerUtil.error("DAO: Помилка під час видалення зарахування з ID: " + id, e);
            throw e;
        }
    }

    // Допоміжний метод перетворення рядка з БД в об'єкт Java
    private Enrollment mapRow(ResultSet rs) throws SQLException {
        Enrollment e = new Enrollment();
        e.setId(rs.getInt("id"));
        e.setCourseId(rs.getInt("course_id"));
        e.setStudentId(rs.getInt("student_id"));
        e.setStatus(EnrollmentStatus.valueOf(rs.getString("status")));
        e.setFactorF1(rs.getInt("factor_f1"));
        e.setFactorF2(rs.getInt("factor_f2"));
        e.setFactorF3(rs.getInt("factor_f3"));
        return e;
    }
}