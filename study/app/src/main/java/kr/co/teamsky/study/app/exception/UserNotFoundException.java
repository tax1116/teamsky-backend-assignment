package kr.co.teamsky.study.app.exception;

public class UserNotFoundException extends BusinessException {

    public UserNotFoundException(Long userId) {
        super(ErrorCode.USER_NOT_FOUND, "userId=" + userId);
    }
}
