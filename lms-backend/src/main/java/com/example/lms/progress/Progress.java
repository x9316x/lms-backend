package com.example.lms.progress;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

@Entity
@Table(name = "progress",
        uniqueConstraints = @UniqueConstraint(name = "uk_progress_user_lesson",
                columnNames = {"user_id","lesson_id"}))
public class Progress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull
    @Column(name = "lesson_id", nullable = false)
    private Long lessonId;

    @Column(name = "watched_sec", nullable = false)
    private int watchedSec = 0;

    @Column(name = "is_completed", nullable = false)
    private boolean completed = false;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist @PreUpdate
    void touch() { this.updatedAt = Instant.now(); }

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getLessonId() { return lessonId; }
    public void setLessonId(Long lessonId) { this.lessonId = lessonId; }

    public int getWatchedSec() { return watchedSec; }
    public void setWatchedSec(int watchedSec) { this.watchedSec = Math.max(0, watchedSec); }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
