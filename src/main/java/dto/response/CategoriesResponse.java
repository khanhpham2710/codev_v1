package dto.response;

import java.util.List;

public record CategoriesResponse(
        boolean success,
        List<Category> data
) {
    public record Category(
            String id,
            String name,
            String slug,
            String icon,
            List<SubCategory> categories
    ) {
    }
}

