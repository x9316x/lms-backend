package com.example.lms.api.admin;

import com.example.lms.course.CourseRepository;
import com.example.lms.module.ModuleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/admin")
public class AdminModuleController {

    private final ModuleRepository modules;
    private final CourseRepository courses;

    public AdminModuleController(ModuleRepository modules, CourseRepository courses) {
        this.modules = modules;
        this.courses = courses;
    }

    /** Создать модуль */
    @PostMapping("/modules")
    @ResponseStatus(HttpStatus.CREATED)
    public com.example.lms.module.Module create(@RequestBody CreateModuleRequest req) {
        var course = courses.findById(req.getCourseId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
        var m = new com.example.lms.module.Module();
        m.setCourse(course);
        m.setTitle(req.getTitle());
        m.setPosition(req.getPosition());
        return modules.save(m);
    }

    /** Обновить модуль (частично) */
    @PutMapping("/modules/{id}")
    public com.example.lms.module.Module update(@PathVariable Long id, @RequestBody UpdateModuleRequest req) {
        var m = modules.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Module not found"));
        if (req.getTitle() != null)    m.setTitle(req.getTitle());
        if (req.getPosition() != null) m.setPosition(req.getPosition());
        return modules.save(m);
    }

    /** Удалить модуль */
    @DeleteMapping("/modules/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!modules.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Module not found");
        }
        modules.deleteById(id);
    }
}
