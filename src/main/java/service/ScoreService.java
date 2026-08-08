package service;

import dto.ScoreDTO;
import entites.ScoreEntity;
import mapper.ScoreMapper;
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

    public List<ScoreDTO> getScoresByUserId(UUID userId) {
        return scoreRepository.findByUserId(userId).stream().map(ScoreMapper::toDTO).toList();
    }
}
