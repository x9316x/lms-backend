package com.example.lms.api;

public class UpdateProgressRequest {
    private Integer watchedSec;
    private Boolean completed;

    public Integer getWatchedSec() { return watchedSec; }
    public void setWatchedSec(Integer watchedSec) { this.watchedSec = watchedSec; }

    public Boolean getCompleted() { return completed; }
    public void setCompleted(Boolean completed) { this.completed = completed; }
}
