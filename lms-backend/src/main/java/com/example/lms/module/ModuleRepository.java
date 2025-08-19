package com.example.lms.module;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ModuleRepository extends JpaRepository<Module, Long> {
    // ВАЖНО: используем course._Id, потому что в сущности поле называется "course"
    List<Module> findAllByCourse_IdOrderByPositionAsc(Long courseId);
}
