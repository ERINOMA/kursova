package com.kursova.model;

public class Assignment {
    private Integer id;
    private Integer courseId;
    private String title;
    private int maxPoints;

    public Assignment() {}

    public Assignment(Integer id, Integer courseId, String title, int maxPoints) {
        this.id = id;
        this.courseId = courseId;
        this.title = title;
        this.maxPoints = maxPoints;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getCourseId() { return courseId; }
    public void setCourseId(Integer courseId) { this.courseId = courseId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getMaxPoints() { return maxPoints; }
    public void setMaxPoints(int maxPoints) { this.maxPoints = maxPoints; }
}
