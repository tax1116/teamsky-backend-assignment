package kr.co.teamsky.study.api.dto.response;

import kr.co.teamsky.study.app.dto.ProblemResult;

public record ProblemResponse(
        Long problemId, String content, String problemType, java.util.List<String> choices, Integer answerCorrectRate) {
    public static ProblemResponse from(ProblemResult result) {
        return new ProblemResponse(
                result.problemId(),
                result.content(),
                result.problemType().name(),
                result.choices(),
                result.answerCorrectRate());
    }
}
