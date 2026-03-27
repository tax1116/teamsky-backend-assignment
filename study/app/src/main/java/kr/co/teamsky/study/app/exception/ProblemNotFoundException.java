package kr.co.teamsky.study.app.exception;

public class ProblemNotFoundException extends BusinessException {

    public ProblemNotFoundException(Long problemId) {
        super(ErrorCode.PROBLEM_NOT_FOUND, "problemId=" + problemId);
    }
}
