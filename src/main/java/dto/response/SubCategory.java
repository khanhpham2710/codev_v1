package dto.response;

import java.util.List;

public record SubCategory(
        String id,
        String name,
        String slug,
        int quizCount,
        List<String> tags
) {
}
