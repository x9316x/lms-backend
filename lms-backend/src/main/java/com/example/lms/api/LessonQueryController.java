package com.example.lms.api;

import com.example.lms.lesson.Lesson;
import com.example.lms.lesson.LessonRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modules")
public class LessonQueryController {

    private final LessonRepository lessonRepository;

    public LessonQueryController(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    @GetMapping("/{moduleId}/lessons")
    public List<Lesson> list(@PathVariable Long moduleId) {
        return lessonRepository.findAllByModuleIdOrderByPositionAsc(moduleId);
    }
}
