package com.example.lms.api;

import com.example.lms.module.Module;
import com.example.lms.module.ModuleRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class ModuleQueryController {

    private final ModuleRepository modules;

    public ModuleQueryController(ModuleRepository modules) {
        this.modules = modules;
    }

    /**
     * Список модулей курса по возрастанию позиции.
     * GET /api/courses/{courseId}/modules
     */
    @GetMapping("/{courseId}/modules")
    public List<Module> list(@PathVariable Long courseId) {
        return modules.findAllByCourse_IdOrderByPositionAsc(courseId);
    }
}
