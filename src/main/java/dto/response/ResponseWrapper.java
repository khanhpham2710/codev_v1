package dto.response;

public record ResponseWrapper<T>(boolean isSuccess, T data) {

    public static <T> ResponseWrapper<T> success(T data) {
        return new ResponseWrapper<>(true, data);
    }

    public static <T> ResponseWrapper<T> failure() {
        return new ResponseWrapper<>(false, null);
    }
}

