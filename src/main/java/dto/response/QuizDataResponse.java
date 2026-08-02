package dto.response;

import java.util.List;

public record QuizDataResponse (boolean success, QuizData data){
    public record QuizData(
            String id,
            String title,
            String description,
            String category,
            String categoryId,
            String categoryName,
            String categorySlug,
            String topic,
            String topicSlug,
            String difficulty,
            List<String> tags,
            int questionCount,
            int plays,
            String slug,
            List<Question> questions
    ) { }

    public record Question(
            String id,
            String text,
            String type,
            String difficulty,
            String explanation,
            int order,
            List<Answer> answers
    ) {}

    public record Answer(
            String id,
            String text,
            boolean isCorrect
    ) {}
}
