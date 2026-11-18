package com.kursova.model;

import java.time.LocalDateTime;

public class Enrollment {

    private Integer id;
    private Integer courseId;
    private Integer studentId;
    private EnrollmentStatus status;
    private int factorF1;
    private int factorF2;
    private int factorF3;
    private double integralScore;
    private LocalDateTime enrollmentDate;

    public Enrollment() {
        this.status = EnrollmentStatus.REQUESTED;
    }

    public void calculateIntegralScore() {
        this.integralScore = (0.40 * this.factorF1) + (0.35 * this.factorF2) + (0.25 * this.factorF3);
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getCourseId() { return courseId; }
    public void setCourseId(Integer courseId) { this.courseId = courseId; }

    public Integer getStudentId() { return studentId; }
    public void setStudentId(Integer studentId) { this.studentId = studentId; }

    public EnrollmentStatus getStatus() { return status; }
    public void setStatus(EnrollmentStatus status) { this.status = status; }

    public int getFactorF1() { return factorF1; }
    public void setFactorF1(int factorF1) {
        this.factorF1 = factorF1;
        calculateIntegralScore();
    }

    public int getFactorF2() { return factorF2; }
    public void setFactorF2(int factorF2) {
        this.factorF2 = factorF2;
        calculateIntegralScore();
    }

    public int getFactorF3() { return factorF3; }
    public void setFactorF3(int factorF3) {
        this.factorF3 = factorF3;
        calculateIntegralScore();
    }

    public double getIntegralScore() { return integralScore; }

    public LocalDateTime getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(LocalDateTime enrollmentDate) { this.enrollmentDate = enrollmentDate; }
}
