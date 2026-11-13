package com.kursova.model;

public class Enrollment {
    private Integer id;
    private Integer courseId;
    private Integer studentId;
    private String status;
    private int factorF1;
    private int factorF2;
    private int factorF3;
    private double integralScore;

    public Enrollment() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getCourseId() { return courseId; }
    public void setCourseId(Integer courseId) { this.courseId = courseId; }

    public Integer getStudentId() { return studentId; }
    public void setStudentId(Integer studentId) { this.studentId = studentId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getFactorF1() { return factorF1; }
    public void setFactorF1(int factorF1) { this.factorF1 = factorF1; }

    public int getFactorF2() { return factorF2; }
    public void setFactorF2(int factorF2) { this.factorF2 = factorF2; }

    public int getFactorF3() { return factorF3; }
    public void setFactorF3(int factorF3) { this.factorF3 = factorF3; }

    public double getIntegralScore() { return integralScore; }
    public void setIntegralScore(double integralScore) { this.integralScore = integralScore; }
}
