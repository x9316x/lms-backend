package com.example.lms.api;

import com.example.lms.course.Course;
import com.example.lms.course.CourseRepository;
import com.example.lms.lesson.Lesson;
import com.example.lms.lesson.LessonRepository;
import com.example.lms.module.Module;
import com.example.lms.module.ModuleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/courses")
public class CourseDetailController {

    private final CourseRepository courses;
    private final ModuleRepository modules;
    private final LessonRepository lessons;

    public CourseDetailController(CourseRepository courses,
                                  ModuleRepository modules,
                                  LessonRepository lessons) {
        this.courses = courses;
        this.modules = modules;
        this.lessons = lessons;
    }

    // Публично: GET /api/courses/{courseId}/detail
    @GetMapping("/{courseId}/detail")
    public CourseDetailDto detail(@PathVariable Long courseId) {
        Course c = courses.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));

        CourseDetailDto dto = new CourseDetailDto();
        dto.setId(c.getId());
        dto.setTitle(c.getTitle());
        dto.setDescription(c.getDescription());
        dto.setPublished(c.isPublished());

        List<Module> moduleList = modules.findAllByCourse_IdOrderByPositionAsc(courseId);
        dto.setModules(
                moduleList.stream().map(m -> {
                    ModuleDto md = new ModuleDto();
                    md.setId(m.getId());
                    md.setTitle(m.getTitle());
                    md.setPosition(m.getPosition());

                    List<Lesson> lessonList = lessons.findAllByModuleIdOrderByPositionAsc(m.getId());
                    md.setLessons(
                            lessonList.stream().map(l -> {
                                LessonDto ld = new LessonDto();
                                ld.setId(l.getId());
                                ld.setTitle(l.getTitle());
                                ld.setPosition(l.getPosition());
                                ld.setDurationSec(l.getDurationSec());
                                return ld;
                            }).collect(Collectors.toList())
                    );

                    return md;
                }).collect(Collectors.toList())
        );

        return dto;
    }

    // --- DTOs ---

    public static class CourseDetailDto {
        private Long id;
        private String title;
        private String description;
        private boolean published;
        private List<ModuleDto> modules;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public boolean isPublished() { return published; }
        public void setPublished(boolean published) { this.published = published; }
        public List<ModuleDto> getModules() { return modules; }
        public void setModules(List<ModuleDto> modules) { this.modules = modules; }
    }

    public static class ModuleDto {
        private Long id;
        private String title;
        private Integer position;
        private List<LessonDto> lessons;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public Integer getPosition() { return position; }
        public void setPosition(Integer position) { this.position = position; }
        public List<LessonDto> getLessons() { return lessons; }
        public void setLessons(List<LessonDto> lessons) { this.lessons = lessons; }
    }

    public static class LessonDto {
        private Long id;
        private String title;
        private Integer position;
        private Integer durationSec;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public Integer getPosition() { return position; }
        public void setPosition(Integer position) { this.position = position; }
        public Integer getDurationSec() { return durationSec; }
        public void setDurationSec(Integer durationSec) { this.durationSec = durationSec; }
    }
}
