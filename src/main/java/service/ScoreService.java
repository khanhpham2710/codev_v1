package service;

import entites.ScoreEntity;
import respositories.ScoreRepository;

import java.util.List;
import java.util.UUID;

public class ScoreService {

    private final ScoreRepository scoreRepository;

    public ScoreService() {
        this.scoreRepository = new ScoreRepository();
    }

    public boolean createScore(ScoreEntity score) {
        return scoreRepository.save(score);
    }

    public List<ScoreEntity> getScoresByUserId(UUID userId) {
        return scoreRepository.findByUserId(userId);
    }
}
