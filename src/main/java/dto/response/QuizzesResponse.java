package dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record QuizzesResponse(boolean success, List<QuizzesOverall> data, PaginationMeta meta) {
}


