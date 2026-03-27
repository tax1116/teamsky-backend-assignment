package kr.co.teamsky.study.api.dto.response;

import java.util.List;
import kr.co.teamsky.study.app.dto.ProblemHistoryResult;

public record ProblemHistoryResponse(
        Long problemId,
        String answerStatus,
        String explanation,
        List<String> problemAnswers,
        List<String> userAnswers,
        Integer answerCorrectRate) {
    public static ProblemHistoryResponse from(ProblemHistoryResult result) {
        return new ProblemHistoryResponse(
                result.problemId(),
                result.answerStatus().name(),
                result.explanation(),
                result.problemAnswers(),
                result.userAnswers(),
                result.answerCorrectRate());
    }
}
