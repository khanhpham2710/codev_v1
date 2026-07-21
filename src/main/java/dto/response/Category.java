package dto.response;

import java.util.List;

public record Category(
        String id,
        String name,
        String slug,
        String icon,
        List<SubCategory> categories
) {
}
