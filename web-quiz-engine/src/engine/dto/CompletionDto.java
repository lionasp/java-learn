package engine.dto;

import java.time.LocalDateTime;

public class CompletionDto {
    private Integer id;
    private LocalDateTime completedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer quizId) {
        this.id = quizId;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
}