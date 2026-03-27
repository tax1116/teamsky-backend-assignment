package kr.co.teamsky.study.infra.persistence.entity;

import jakarta.persistence.*;
import java.util.List;
import kr.co.teamsky.study.domain.model.AnswerStatus;
import kr.co.teamsky.study.domain.model.Submission;
import kr.co.teamsky.study.domain.model.id.ProblemId;
import kr.co.teamsky.study.domain.model.id.SubmissionId;
import kr.co.teamsky.study.domain.model.id.UserId;
import kr.co.teamsky.study.infra.persistence.converter.StringListConverter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "submission", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "problem_id"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubmissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long problemId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnswerStatus answerStatus;

    @Convert(converter = StringListConverter.class)
    @Column(nullable = false)
    private List<String> userAnswers;

    public static SubmissionEntity from(Submission submission) {
        SubmissionEntity entity = new SubmissionEntity();
        entity.userId = submission.userId().value();
        entity.problemId = submission.problemId().value();
        entity.answerStatus = submission.answerStatus();
        entity.userAnswers = submission.userAnswers();
        return entity;
    }

    public Submission toDomain() {
        return new Submission(
                new SubmissionId(id), new UserId(userId), new ProblemId(problemId), answerStatus, userAnswers);
    }
}
