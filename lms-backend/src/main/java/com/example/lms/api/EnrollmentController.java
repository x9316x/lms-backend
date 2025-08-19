package com.example.lms.api;

import com.example.lms.auth.CurrentUser;
import com.example.lms.course.Course;
import com.example.lms.course.CourseRepository;
import com.example.lms.enroll.Enrollment;
import com.example.lms.enroll.EnrollmentRepository;
import com.example.lms.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class EnrollmentController {

    private final EnrollmentRepository enrollments;
    private final CourseRepository courses;
    private final CurrentUser current;

    public EnrollmentController(EnrollmentRepository enrollments, CourseRepository courses, CurrentUser current) {
        this.enrollments = enrollments;
        this.courses = courses;
        this.current = current;
    }

    @PostMapping("/enroll/{courseId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> enroll(@PathVariable Long courseId) {
        User user = current.require();
        courses.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));

        if (enrollments.existsByUserIdAndCourseId(user.getId(), courseId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Already enrolled");
        }

        Enrollment e = new Enrollment();
        e.setUserId(user.getId());
        e.setCourseId(courseId);
        enrollments.save(e);

        return Map.of("status", "enrolled", "courseId", courseId, "userId", user.getId());
    }

    @GetMapping("/my/courses")
    public List<Course> myCourses() {
        User user = current.require();
        var userEnrolls = enrollments.findAllByUserId(user.getId());
        if (userEnrolls.isEmpty()) return List.of();
        var ids = userEnrolls.stream().map(Enrollment::getCourseId).toList();
        var list = courses.findAllById(ids);
        list.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
        return list;
    }
}
