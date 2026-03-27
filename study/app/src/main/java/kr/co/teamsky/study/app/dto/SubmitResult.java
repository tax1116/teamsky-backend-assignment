package kr.co.teamsky.study.app.dto;

import java.util.List;
import kr.co.teamsky.study.domain.model.AnswerStatus;

public record SubmitResult(
        Long problemId, AnswerStatus answerStatus, String explanation, List<String> problemAnswers) {}
