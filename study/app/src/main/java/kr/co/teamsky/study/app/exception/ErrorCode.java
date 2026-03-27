package kr.co.teamsky.study.app.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    PROBLEM_NOT_FOUND(4001, 404, "문제를 찾을 수 없습니다."),
    SUBMISSION_NOT_FOUND(4002, 404, "풀이 이력을 찾을 수 없습니다."),
    NO_PROBLEMS_AVAILABLE(4003, 404, "해당 단원에 풀 수 있는 문제가 없습니다."),
    DUPLICATE_SUBMISSION(4004, 409, "이미 제출한 문제입니다."),
    INVALID_ANSWER_REQUEST(4005, 400, "답안 요청 형식이 올바르지 않습니다."),
    USER_NOT_FOUND(4006, 404, "사용자를 찾을 수 없습니다.");

    private final int code;
    private final int httpStatus;
    private final String message;
}
