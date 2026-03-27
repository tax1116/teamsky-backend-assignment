package kr.co.teamsky.study.domain.model;

import java.util.List;
import kr.co.teamsky.study.domain.model.id.ProblemId;
import kr.co.teamsky.study.domain.model.id.SubmissionId;
import kr.co.teamsky.study.domain.model.id.UserId;

public record Submission(
        SubmissionId id, UserId userId, ProblemId problemId, AnswerStatus answerStatus, List<String> userAnswers) {}
