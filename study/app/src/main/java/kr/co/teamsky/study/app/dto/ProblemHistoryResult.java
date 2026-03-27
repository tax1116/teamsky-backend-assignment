package kr.co.teamsky.study.app.dto;

import java.util.List;
import kr.co.teamsky.study.domain.model.AnswerStatus;

public record ProblemHistoryResult(
        Long problemId,
        AnswerStatus answerStatus,
        String explanation,
        List<String> problemAnswers,
        List<String> userAnswers,
        Integer answerCorrectRate) {}
