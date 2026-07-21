package dto.response;

import java.util.List;

public record CategoriesResponse(
        boolean success,
        List<Category> data
) {
}

