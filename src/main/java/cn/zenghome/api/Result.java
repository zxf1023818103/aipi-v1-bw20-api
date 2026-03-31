package cn.zenghome.api;

public record Result<T>(boolean success, T data, String message) {

    public static <T> Result<T> ok(T data) {
        return new Result<>(true, data, null);
    }

    public static <T> Result<T> fail(String message) {
        return new Result<>(false, null, message);
    }
}
