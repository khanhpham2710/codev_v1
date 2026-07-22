package dto.response;

import enums.EDifficulty;

import java.util.List;

public record QuizzesOverall(String id, String title, String description, String category, String categoryId,
                             String categoryName, String categorySlug, String topic, String topicSlug,
                             EDifficulty difficulty,
                             List<String> tags, Integer questionCount, Double plays, String slug) {

}
