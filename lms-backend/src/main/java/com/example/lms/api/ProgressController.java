package com.example.lms.api;

import com.example.lms.auth.CurrentUser;
import com.example.lms.lesson.LessonRepository;
import com.example.lms.progress.Progress;
import com.example.lms.progress.ProgressRepository;
import com.example.lms.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ProgressController {

    private final ProgressRepository progressRepo;
    private final LessonRepository lessonRepo;
    private final CurrentUser current;

    public ProgressController(ProgressRepository progressRepo, LessonRepository lessonRepo, CurrentUser current) {
        this.progressRepo = progressRepo;
        this.lessonRepo = lessonRepo;
        this.current = current;
    }

    @PostMapping("/progress/{lessonId}")
    public Map<String, Object> upsertProgress(@PathVariable Long lessonId,
                                              @RequestBody UpdateProgressRequest req) {
        User user = current.require();

        lessonRepo.findById(lessonId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lesson not found"));

        Progress p = progressRepo.findByUserIdAndLessonId(user.getId(), lessonId)
                .orElseGet(() -> {
                    Progress np = new Progress();
                    np.setUserId(user.getId());
                    np.setLessonId(lessonId);
                    return np;
                });

        if (req.getWatchedSec() != null) p.setWatchedSec(Math.max(0, req.getWatchedSec()));
        if (req.getCompleted() != null) p.setCompleted(req.getCompleted());

        Progress saved = progressRepo.save(p);
        return Map.of(
                "lessonId", saved.getLessonId(),
                "userId", saved.getUserId(),
                "watchedSec", saved.getWatchedSec(),
                "completed", saved.isCompleted(),
                "updatedAt", saved.getUpdatedAt()
        );
    }

    @GetMapping("/my/progress")
    public List<Progress> myProgress() {
        User user = current.require();
        return progressRepo.findAllByUserId(user.getId());
    }
}
