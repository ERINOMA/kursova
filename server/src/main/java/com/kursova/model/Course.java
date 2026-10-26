package com.kursova.model;

public class Course {
    private Integer id;
    private String title;
    private String description;
    private int durationHours;
    private double price;

    public Course() {}

    public Course(Integer id, String title, String description, int durationHours, double price) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.durationHours = durationHours;
        this.price = price;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getDurationHours() { return durationHours; }
    public void setDurationHours(int durationHours) { this.durationHours = durationHours; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}
