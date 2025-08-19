package com.example.lms.api.admin;

import com.example.lms.course.Course;
import com.example.lms.course.CourseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/admin")
public class AdminCourseController {

    private final CourseRepository courses;

    public AdminCourseController(CourseRepository courses) {
        this.courses = courses;
    }

    /** Создать курс */
    @PostMapping("/courses")
    @ResponseStatus(HttpStatus.CREATED)
    public Course create(@RequestBody CreateCourseRequest req) {
        Course c = new Course();
        c.setTitle(req.getTitle());
        c.setDescription(req.getDescription());
        // published по умолчанию false, если не прислали
        boolean published = Boolean.TRUE.equals(req.getPublished());
        c.setPublished(published);
        return courses.save(c);
    }

    /** Обновить курс частично (title/description/published) */
    @PutMapping("/courses/{id}")
    public Course update(@PathVariable Long id, @RequestBody UpdateCourseRequest req) {
        Course c = courses.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));

        if (req.getTitle() != null) c.setTitle(req.getTitle());
        if (req.getDescription() != null) c.setDescription(req.getDescription());
        if (req.getPublished() != null) c.setPublished(req.getPublished());

        return courses.save(c);
    }

    /** Удалить курс */
    @DeleteMapping("/courses/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!courses.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found");
        }
        courses.deleteById(id);
    }
}
