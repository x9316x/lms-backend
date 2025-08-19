package com.example.lms.api.admin;

public class UpdateCourseRequest {
    private String title;
    private String description;
    private Boolean published;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Boolean getPublished() { return published; }
    public void setPublished(Boolean published) { this.published = published; }
}
