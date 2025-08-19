package com.example.lms.api;

import com.example.lms.course.CourseService;
import com.example.lms.course.dto.CourseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    // Публичный список опубликованных курсов
    @GetMapping("/courses")
    public List<CourseDto> listPublished() {
        return courseService.getPublished();
    }
}
