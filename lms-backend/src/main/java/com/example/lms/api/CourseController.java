package com.example.lms.api;

import com.example.lms.course.Course;
import com.example.lms.course.CourseRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CourseController {

    private final CourseRepository courseRepository;

    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    // Публичный список опубликованных курсов
    @GetMapping("/courses")
    public List<Course> listPublished() {
        return courseRepository.findAllByPublishedTrueOrderByCreatedAtDesc();
    }
}
