package com.example.lms.api;

import com.example.lms.auth.CurrentUser;
import com.example.lms.enroll.EnrollmentRepository;
import com.example.lms.lesson.Lesson;
import com.example.lms.lesson.LessonRepository;
import com.example.lms.module.ModuleRepository;
import com.example.lms.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/lessons")
public class LessonWatchController {

    private final LessonRepository lessons;
    private final ModuleRepository modules;
    private final EnrollmentRepository enrollments;
    private final CurrentUser current;

    public LessonWatchController(LessonRepository lessons,
                                 ModuleRepository modules,
                                 EnrollmentRepository enrollments,
                                 CurrentUser current) {
        this.lessons = lessons;
        this.modules = modules;
        this.enrollments = enrollments;
        this.current = current;
    }

    @GetMapping("/{lessonId}/watch-url")
    public Map<String, Object> watchUrl(@PathVariable Long lessonId) {
        User u = current.require(); // 401, если нет JWT

        Lesson l = lessons.findById(lessonId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lesson not found"));

        var m = modules.findById(l.getModuleId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Module not found"));

        boolean isEnrolled = enrollments.existsByUserIdAndCourseId(u.getId(), m.getCourseId());
        if (!isEnrolled) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not enrolled to this course");

        if (l.getVideoUrl() == null || l.getVideoUrl().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Video URL is not set");
        }
        return Map.of("lessonId", l.getId(), "url", l.getVideoUrl());
    }
}
