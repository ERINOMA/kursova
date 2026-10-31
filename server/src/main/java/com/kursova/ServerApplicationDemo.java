package com.kursova;

import com.kursova.model.Enrollment;
import com.kursova.model.EnrollmentStatus;
import com.kursova.service.EnrollmentService;
import com.kursova.util.LoggerUtil;

public class ServerApplicationDemo {
    public static void main(String[] args) {
        EnrollmentService service = new EnrollmentService();

        LoggerUtil.info("=== ЗАПУСК ДЕМОНСТРАЦІЇ СЕРВЕРНОЇ ЛОГІКИ ===");
        LoggerUtil.info("Перевірка: Система керування курсами та зарахуванням");

        try {
            Enrollment demoEnrollment = new Enrollment();
            demoEnrollment.setCourseId(1);
            demoEnrollment.setStudentId(1);

            service.registerNewEnrollment(demoEnrollment);
            int enrollmentId = demoEnrollment.getId();
            LoggerUtil.info("Присвоєно ID з бази даних: " + enrollmentId);

            LoggerUtil.info("Спроба перевести в стан APPROVED...");
            service.changeStatus(enrollmentId, EnrollmentStatus.APPROVED);

            LoggerUtil.info("Спроба перевести в стан ACTIVE...");
            service.changeStatus(enrollmentId, EnrollmentStatus.ACTIVE);

            // Оцінки: F1=90, F2=85, F3=95
            // Формула: I = 0.40*90 + 0.35*85 + 0.25*95 = 36 + 29.75 + 23.75 = 89.50
            LoggerUtil.info("Виставляємо оцінки факторів F1, F2, F3...");
            service.updateFactors(enrollmentId, 90, 85, 95);

            LoggerUtil.info("Спроба перевести в фінальний стан COMPLETED...");
            service.changeStatus(enrollmentId, EnrollmentStatus.COMPLETED);

            System.out.println("\n[ТЕСТ] Спроба порушити послідовність станів (COMPLETED -> REQUESTED):");
            try {
                service.changeStatus(enrollmentId, EnrollmentStatus.REQUESTED);
                System.out.println("ТЕСТ ПРОВАЛЕНО! Пропущено неправильний перехід.");
            } catch (IllegalStateException ex) {
                System.out.println("ТЕСТ УСПІШНИЙ! Заблоковано хаотичну зміну статусу.");
            }

            LoggerUtil.info("=== ДЕМОНСТРАЦІЯ ЗАВЕРШЕНА УСПІШНО! ВСЕ ПРАЦЮЄ ===");

        } catch (Exception e) {
            LoggerUtil.error("Помилка під час виконання демонстраційного сценарію", e);
        }
    }
}
