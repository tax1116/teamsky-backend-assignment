package kr.co.teamsky.study.app.dto;

import java.util.List;
import kr.co.teamsky.study.domain.model.Choice;
import kr.co.teamsky.study.domain.model.Problem;
import kr.co.teamsky.study.domain.model.ProblemType;

public record ProblemResult(
        Long problemId, String content, ProblemType problemType, List<String> choices, Integer answerCorrectRate) {
    public static ProblemResult of(Problem problem, List<Choice> choices, Integer correctRate) {
        return new ProblemResult(
                problem.id().value(),
                problem.content(),
                problem.problemType(),
                choices.stream().map(Choice::content).toList(),
                correctRate);
    }
}
