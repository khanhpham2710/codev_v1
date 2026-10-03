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

    public LearningStatistics load(UUID userId, LocalDate from, LocalDate to)
            throws SQLException {

        Objects.requireNonNull(userId, "A logged-in user is required.");

        if (from != null && to != null && from.isAfter(to)) {
            throw new IllegalArgumentException(
                    "From date must be on or before To date."
            );
        }

        Map<String, ScoreEntity> bestByQuiz = new HashMap<>();
        Map<String, Long> attemptsByQuiz = new HashMap<>();
        long totalAttempts = 0;

        Comparator<ScoreEntity> ranking = Comparator
                .comparingInt(ScoreEntity::getScore)
                .thenComparing(ScoreEntity::getCreatedAt);

        for (ScoreEntity score : repository.findByUserIdOrThrow(userId)) {
            LocalDate date = score.getCreatedAt().atZone(zone).toLocalDate();

            if ((from != null && date.isBefore(from))
                    || (to != null && date.isAfter(to))) {
                continue;
            }

            totalAttempts++;
            attemptsByQuiz.merge(score.getQuizId(), 1L, Long::sum);

            bestByQuiz.merge(
                    score.getQuizId(),
                    score,
                    (existing, candidate) ->
                            ranking.compare(candidate, existing) > 0
                                    ? candidate
                                    : existing
            );
        }

        List<ScoreEntity> bestScores = bestByQuiz.values().stream()
                .sorted(Comparator.comparing(ScoreEntity::getCreatedAt).reversed())
                .toList();

        List<LearningStatistics.DailyResult> results = new ArrayList<>();
        long correct = 0;

        for (ScoreEntity score : bestScores) {
            correct += score.getScore();

            results.add(new LearningStatistics.DailyResult(
                    score.getCreatedAt().atZone(zone).toLocalDate(),
                    attemptsByQuiz.get(score.getQuizId()),
                    score.getQuizId(),
                    score.getScore()
            ));
        }

        return new LearningStatistics(
                totalAttempts,
                bestByQuiz.size(),
                correct,
                results
        );
    }
}
