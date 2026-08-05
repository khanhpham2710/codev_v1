package mapper;

import dto.ScoreDTO;
import entites.ScoreEntity;

public final class ScoreMapper {
    public static ScoreDTO toDTO(ScoreEntity entity) {

        if (entity == null) {
            return null;
        }

        ScoreDTO dto = new ScoreDTO();

        dto.setId(entity.getId());
        dto.setQuizId(entity.getQuizId());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUserId(entity.getUserId());
        dto.setScore(entity.getScore());

        return dto;
    }

    // DTO -> Entity
    public static ScoreEntity toEntity(ScoreDTO dto) {

        if (dto == null) {
            return null;
        }

        return new ScoreEntity.Builder()
                .id(dto.getId())
                .quizId(dto.getQuizId())
                .userId(dto.getUserId())
                .score(dto.getScore())
                .build();
    }
}
