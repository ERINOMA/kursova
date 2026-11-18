package com.kursova;

import com.kursova.dao.EnrollmentDao;
import com.kursova.dao.StudentDao;
import com.kursova.model.Enrollment;
import com.kursova.model.EnrollmentStatus;
import com.kursova.service.EnrollmentService;
import com.kursova.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CourseManagementBusinessTest {

    private EnrollmentService enrollmentService;
    private StudentService studentService;
    private InMemoryEnrollmentDao fakeDao;

    static class InMemoryEnrollmentDao extends EnrollmentDao {
        private final Map<Integer, Enrollment> storage = new HashMap<>();

        @Override
        public void create(Enrollment e) {
            e.setId(storage.size() + 1);
            storage.put(e.getId(), e);
        }

        @Override
        public Enrollment findById(int id) {
            return storage.get(id);
        }

        @Override
        public void update(Enrollment e) {
            storage.put(e.getId(), e);
        }
    }

    @BeforeEach
    void setUp() {
        fakeDao = new InMemoryEnrollmentDao();
        enrollmentService = new EnrollmentService(fakeDao);
        studentService = new StudentService(new StudentDao());
    }

    // Тест 1:
    @Test
    void test1_IntegralScoreCalculationFormula() {
        Enrollment e = new Enrollment();
        e.setFactorF1(100); // 40.0
        e.setFactorF2(80);  // 28.0
        e.setFactorF3(60);  // 15.0
        assertEquals(83.0, e.getIntegralScore(), 0.001, "показник повинен дорівнювати 83.0");
    }

    // Тест 2:
    @Test
    void test2_IntegralScoreZeroFactors() {
        Enrollment e = new Enrollment();
        e.setFactorF1(0);
        e.setFactorF2(0);
        e.setFactorF3(0);
        assertEquals(0.0, e.getIntegralScore(), 0.001);
    }

    // Тест 3:
    @Test
    void test3_FactorValidationRejectsNegative() throws Exception {
        Enrollment e = new Enrollment();
        e.setCourseId(1);
        e.setStudentId(1);
        enrollmentService.registerNewEnrollment(e);

        assertThrows(IllegalArgumentException.class, () ->
                enrollmentService.updateFactors(e.getId(), -5, 50, 50)
        );
    }

    // Тест 4:
    @Test
    void test4_FactorValidationRejectsOver100() throws Exception {
        Enrollment e = new Enrollment();
        e.setCourseId(1);
        e.setStudentId(1);
        enrollmentService.registerNewEnrollment(e);

        assertThrows(IllegalArgumentException.class, () ->
                enrollmentService.updateFactors(e.getId(), 50, 105, 50)
        );
    }

    // Тест 5:
    @Test
    void test5_InitialEnrollmentStatusIsRequested() throws Exception {
        Enrollment e = new Enrollment();
        e.setCourseId(1);
        e.setStudentId(2);
        enrollmentService.registerNewEnrollment(e);

        assertEquals(EnrollmentStatus.REQUESTED, fakeDao.findById(e.getId()).getStatus());
    }

    // Тест 6:
    @Test
    void test6_ValidLifecycleTransitionSequence() throws Exception {
        Enrollment e = new Enrollment();
        e.setCourseId(1);
        e.setStudentId(1);
        enrollmentService.registerNewEnrollment(e);

        enrollmentService.changeStatus(e.getId(), EnrollmentStatus.APPROVED);
        assertEquals(EnrollmentStatus.APPROVED, fakeDao.findById(e.getId()).getStatus());

        enrollmentService.changeStatus(e.getId(), EnrollmentStatus.ACTIVE);
        assertEquals(EnrollmentStatus.ACTIVE, fakeDao.findById(e.getId()).getStatus());

        enrollmentService.changeStatus(e.getId(), EnrollmentStatus.COMPLETED);
        assertEquals(EnrollmentStatus.COMPLETED, fakeDao.findById(e.getId()).getStatus());
    }

    // Тест 7:
    @Test
    void test7_ForbiddenLifecycleJumpRequestedToCompleted() throws Exception {
        Enrollment e = new Enrollment();
        e.setCourseId(1);
        e.setStudentId(1);
        enrollmentService.registerNewEnrollment(e);

        assertThrows(IllegalStateException.class, () ->
                enrollmentService.changeStatus(e.getId(), EnrollmentStatus.COMPLETED)
        );
    }

    // Тест 8:
    @Test
    void test8_ForbiddenLifecycleBackwardTransition() throws Exception {
        Enrollment e = new Enrollment();
        e.setCourseId(1);
        e.setStudentId(1);
        enrollmentService.registerNewEnrollment(e);
        enrollmentService.changeStatus(e.getId(), EnrollmentStatus.APPROVED);

        assertThrows(IllegalStateException.class, () ->
                enrollmentService.changeStatus(e.getId(), EnrollmentStatus.REQUESTED)
        );
    }

    // Тест 9:
    @Test
    void test9_EnrollmentValidationMissingForeignKeys() {
        Enrollment invalid = new Enrollment();
        assertThrows(IllegalArgumentException.class, () ->
                enrollmentService.registerNewEnrollment(invalid)
        );
    }

    // Тест 10:
    @Test
    void test10_StudentDataValidation() {
        assertDoesNotThrow(() ->
                studentService.validateStudentData("Максим", "Салат", "max@example.com", LocalDate.now())
        );
        assertThrows(IllegalArgumentException.class, () ->
                studentService.validateStudentData("", "Салат", "bad-email", LocalDate.now())
        );
    }
}
