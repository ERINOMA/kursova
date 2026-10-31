package com.kursova.service;

import com.kursova.dao.EnrollmentDao;
import com.kursova.model.Enrollment;
import com.kursova.model.EnrollmentStatus;
import com.kursova.util.LoggerUtil;

import java.sql.SQLException;
import java.util.List;

public class EnrollmentService {
    private final EnrollmentDao enrollmentDao = new EnrollmentDao();

    /**
     * Реєстрація нового запиту для зарахування
     */
    public void registerNewEnrollment(Enrollment enrollment) throws SQLException {
        // 1. Валідація обов'язкових зв'язків
        if (enrollment.getCourseId() == null || enrollment.getStudentId() == null) {
            String error = "Помилка: ID курсу та студента є обов'язковими!";
            LoggerUtil.error(error, new IllegalArgumentException(error));
            throw new IllegalArgumentException(error);
        }

        enrollment.setStatus(EnrollmentStatus.REQUESTED);
        enrollment.setFactorF1(0);
        enrollment.setFactorF2(0);
        enrollment.setFactorF3(0);

        enrollmentDao.create(enrollment);
        LoggerUtil.info("СЕРВІС: Створено новий запит на зарахування у стані REQUESTED.");
    }

    /**
     * Контроль 4-станного життєвого циклу
     */
    public void changeStatus(int enrollmentId, EnrollmentStatus newStatus) throws SQLException {
        Enrollment enrollment = enrollmentDao.findById(enrollmentId);
        if (enrollment == null) {
            throw new IllegalArgumentException("Зарахування з ID " + enrollmentId + " не знайдено!");
        }

        EnrollmentStatus currentStatus = enrollment.getStatus();
        boolean isValidTransition = false;

        switch (currentStatus) {
            case REQUESTED:
                if (newStatus == EnrollmentStatus.APPROVED) isValidTransition = true;
                break;
            case APPROVED:
                if (newStatus == EnrollmentStatus.ACTIVE) isValidTransition = true;
                break;
            case ACTIVE:
                if (newStatus == EnrollmentStatus.COMPLETED) isValidTransition = true;
                break;
            case COMPLETED:
                break;
        }

        if (!isValidTransition) {
            String errorMsg = String.format("Порушення! Недопустимий перехід: з %s у %s", currentStatus, newStatus);
            LoggerUtil.error(errorMsg, new IllegalStateException(errorMsg));
            throw new IllegalStateException(errorMsg);
        }

        enrollment.setStatus(newStatus);
        enrollmentDao.update(enrollment);
        LoggerUtil.info(String.format("СЕРВІС: Об'єкт ID %d успішно переведено в новий стан: %s", enrollmentId, newStatus));
    }

    /**
     *Фактори F1, F2, F3 (0-100) та збереження
     */
    public void updateFactors(int enrollmentId, int f1, int f2, int f3) throws SQLException {
        // Перевірка допустимого числового діапазону нормованих факторів
        if (f1 < 0 || f1 > 100 || f2 < 0 || f2 > 100 || f3 < 0 || f3 > 100) {
            String error = "Помилка: Значення факторів F1, F2, F3 мають бути в межах від 0 до 100!";
            LoggerUtil.error(error, new IllegalArgumentException(error));
            throw new IllegalArgumentException(error);
        }

        Enrollment enrollment = enrollmentDao.findById(enrollmentId);
        if (enrollment == null) {
            throw new IllegalArgumentException("Зарахування не існує!");
        }


        enrollment.setFactorF1(f1);
        enrollment.setFactorF2(f2);
        enrollment.setFactorF3(f3);

        enrollmentDao.update(enrollment);
        LoggerUtil.info(String.format("СЕРВІС: Оновлено оцінки для ID %d. Автоматично розрахований інтегральний бал: %.2f",
                enrollmentId, enrollment.getIntegralScore()));
    }

    public List<Enrollment> getAllEnrollments() throws SQLException {
        return enrollmentDao.findAll();
    }
}