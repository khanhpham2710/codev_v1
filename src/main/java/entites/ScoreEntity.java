package entites;

import java.time.Instant;
import java.util.UUID;

public class ScoreEntity {

    private final UUID id;
    private final String quizId;
    private final Instant createdAt;
    private final UUID userId;
    private final Integer score;

    private ScoreEntity(UUID id, String quizId, UUID userId, Integer score) {
        this.id = (id != null) ? id : UUID.randomUUID();
        this.quizId = quizId;
        this.userId = userId;
        this.createdAt = Instant.now();
        this.score = (score != null) ? score : 0;

        validate();
    }

    private void validate() {

        if (quizId == null) {
            throw new IllegalArgumentException("Quiz id is required.");
        }

        if (userId == null) {
            throw new IllegalArgumentException("User id is required.");
        }

        if (score == null) {
            throw new IllegalArgumentException("Score is required.");
        }

        if (score < 0) {
            throw new IllegalArgumentException("Score must be greater than or equal to 0.");
        }
    }

    public static class Builder {

        private UUID id;
        private String quizId;
        private UUID userId;
        private Integer score;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder quizId(String quizId) {
            this.quizId = quizId;
            return this;
        }

        public Builder userId(UUID userId) {
            this.userId = userId;
            return this;
        }

        public Builder score(Integer score) {
            this.score = score;
            return this;
        }

        public ScoreEntity build() {
            return new ScoreEntity(id, quizId, userId, score);
        }
    }

    public UUID getId(){
        return id;
    }

    public String getQuizId() {
        return quizId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public UUID getUserId() {
        return userId;
    }

    public Integer getScore(){
        return score;
    }
}