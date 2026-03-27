package kr.co.teamsky.study.app.exception;

public class DuplicateSubmissionException extends BusinessException {

    public DuplicateSubmissionException(Long userId, Long problemId) {
        this(userId, problemId, null);
    }

    public DuplicateSubmissionException(Long userId, Long problemId, Throwable cause) {
        super(
                ErrorCode.DUPLICATE_SUBMISSION,
                userId != null && problemId != null ? "userId=" + userId + ", problemId=" + problemId : "",
                cause);
    }
}
