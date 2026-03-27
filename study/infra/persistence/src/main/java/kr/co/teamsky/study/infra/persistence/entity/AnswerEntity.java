package kr.co.teamsky.study.infra.persistence.entity;

import jakarta.persistence.*;
import kr.co.teamsky.study.domain.model.Answer;
import kr.co.teamsky.study.domain.model.id.AnswerId;
import kr.co.teamsky.study.domain.model.id.ProblemId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "answer")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnswerEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long problemId;

    @Column(nullable = false)
    private String answer;

    public Answer toDomain() {
        return new Answer(new AnswerId(id), new ProblemId(problemId), answer);
    }
}
