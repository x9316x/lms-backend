package com.example.lms.api.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateLessonRequest {
    @NotNull
    private Long moduleId;
    @NotBlank
    private String title;

    private String description;
    private String videoUrl;   // пока прямой URL, позже сделаем S3 key + presigned
    private Integer durationSec;
    private Integer position;

    public Long getModuleId() { return moduleId; }
    public void setModuleId(Long moduleId) { this.moduleId = moduleId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

    public Integer getDurationSec() { return durationSec; }
    public void setDurationSec(Integer durationSec) { this.durationSec = durationSec; }

    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position = position; }
}
