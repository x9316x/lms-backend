package com.example.lms.api.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateModuleRequest {
    @NotNull
    private Long courseId;

    @NotBlank
    private String title;

    // порядок внутри курса (0 по умолчанию)
    private Integer position;

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position = position; }
}
