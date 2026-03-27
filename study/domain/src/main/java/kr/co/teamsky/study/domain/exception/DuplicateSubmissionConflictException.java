package kr.co.teamsky.study.domain.exception;

public class DuplicateSubmissionConflictException extends RuntimeException {

    private final Long userId;
    private final Long problemId;

    public DuplicateSubmissionConflictException(Long userId, Long problemId, Throwable cause) {
        super("Duplicate submission conflict", cause);
        this.userId = userId;
        this.problemId = problemId;
    }

    public Long userId() {
        return userId;
    }

    public Long problemId() {
        return problemId;
    }
}
