package kr.co.teamsky.study.app.exception;

public class NoProblemAvailableException extends BusinessException {

    public NoProblemAvailableException() {
        super(ErrorCode.NO_PROBLEMS_AVAILABLE);
    }
}
