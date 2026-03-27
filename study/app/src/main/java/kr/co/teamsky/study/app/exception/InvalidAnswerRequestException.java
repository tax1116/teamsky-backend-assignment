package kr.co.teamsky.study.app.exception;

public class InvalidAnswerRequestException extends BusinessException {

    public InvalidAnswerRequestException(String detail) {
        super(ErrorCode.INVALID_ANSWER_REQUEST, detail);
    }
}
