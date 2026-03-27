package kr.co.teamsky.study.app.exception;

public class SubmissionNotFoundException extends BusinessException {

    public SubmissionNotFoundException(Long userId, Long problemId) {
        super(ErrorCode.SUBMISSION_NOT_FOUND, "userId=" + userId + ", problemId=" + problemId);
    }
}
