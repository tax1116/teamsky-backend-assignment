package kr.co.teamsky.study.api.dto.response;

import java.util.Map;

public record ErrorResponse(int code, String message, Map<String, String> errors) {
    public static ErrorResponse of(int code, String message) {
        return new ErrorResponse(code, message, null);
    }

    public static ErrorResponse of(int code, String message, Map<String, String> errors) {
        return new ErrorResponse(code, message, errors);
    }
}
