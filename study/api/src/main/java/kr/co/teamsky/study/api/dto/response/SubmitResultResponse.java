package kr.co.teamsky.study.api.dto.response;

import java.util.List;
import kr.co.teamsky.study.app.dto.SubmitResult;

public record SubmitResultResponse(
        Long problemId, String answerStatus, String explanation, List<String> problemAnswers) {
    public static SubmitResultResponse from(SubmitResult result) {
        return new SubmitResultResponse(
                result.problemId(), result.answerStatus().name(), result.explanation(), result.problemAnswers());
    }
}
