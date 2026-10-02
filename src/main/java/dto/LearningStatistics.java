package dto;

import java.time.LocalDate;
import java.util.List;

public record LearningStatistics(long attempts, long quizzes, long correctAnswers,
                                 List<DailyResult> days) {
    public LearningStatistics {
        days = List.copyOf(days);
    }

    public record DailyResult(LocalDate date, long attempts, long quizzes, long correctAnswers) {}
}
