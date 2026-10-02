package service;

import dto.LearningStatistics;
import entites.ScoreEntity;
import respositories.ScoreRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class LearningStatisticsService {
    private final ScoreRepository repository;
    private final ZoneId zone;

    public LearningStatisticsService() {
        this(new ScoreRepository(), ZoneId.systemDefault());
    }

    public LearningStatisticsService(ScoreRepository repository, ZoneId zone) {
        this.repository = Objects.requireNonNull(repository);
        this.zone = Objects.requireNonNull(zone);
    }

    public LearningStatistics load(UUID userId, LocalDate from, LocalDate to) throws SQLException {
        Objects.requireNonNull(userId, "A logged-in user is required.");
        if (from != null && to != null && from.isAfter(to)) {
            throw new IllegalArgumentException("From date must be on or before To date.");
        }
        Map<LocalDate, List<ScoreEntity>> groups = new TreeMap<>(Comparator.reverseOrder());
        Set<String> quizzes = new HashSet<>();
        long attempts = 0, correct = 0;
        for (ScoreEntity score : repository.findByUserIdOrThrow(userId)) {
            LocalDate date = score.getCreatedAt().atZone(zone).toLocalDate();
            if ((from != null && date.isBefore(from)) || (to != null && date.isAfter(to))) continue;
            groups.computeIfAbsent(date, key -> new ArrayList<>()).add(score);
            attempts++;
            correct += score.getScore();
            quizzes.add(score.getQuizId());
        }
        List<LearningStatistics.DailyResult> days = new ArrayList<>();
        groups.forEach((date, scores) -> days.add(new LearningStatistics.DailyResult(
                date, scores.size(), scores.stream().map(ScoreEntity::getQuizId).distinct().count(),
                scores.stream().mapToLong(ScoreEntity::getScore).sum())));
        return new LearningStatistics(attempts, quizzes.size(), correct, days);
    }
}
