package com.assignment.Course.Entity;


import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "courses")
public class CourseDocument {
    private String id;
    private String title;
    private String category;
    private String type;
    private int minAge;
    private int maxAge;
    private double price;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate nextSessionDate;

    public CourseDocument() {}

    public CourseDocument(String id, String title, String category, String type,
                          int minAge, int maxAge, double price, LocalDate nextSessionDate) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.type = type;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.price = price;
        this.nextSessionDate = nextSessionDate;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public int getMinAge() { return minAge; }
    public void setMinAge(int minAge) { this.minAge = minAge; }

    public int getMaxAge() { return maxAge; }
    public void setMaxAge(int maxAge) { this.maxAge = maxAge; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public LocalDate getNextSessionDate() { return nextSessionDate; }
    public void setNextSessionDate(LocalDate nextSessionDate) { this.nextSessionDate = nextSessionDate; }
}