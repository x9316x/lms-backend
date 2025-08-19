package com.example.lms.api.admin;

import com.example.lms.lesson.Lesson;
import com.example.lms.lesson.LessonRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/lessons")
public class AdminLessonController {

    private final LessonRepository lessonRepository;

    public AdminLessonController(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Lesson create(@RequestBody CreateLessonRequest req) {
        Lesson l = new Lesson();
        l.setModuleId(req.getModuleId());
        l.setTitle(req.getTitle());
        l.setDescription(req.getDescription());
        l.setVideoUrl(req.getVideoUrl());
        l.setDurationSec(req.getDurationSec());
        l.setPosition(req.getPosition() != null ? req.getPosition() : 0);
        return lessonRepository.save(l);
    }
}
