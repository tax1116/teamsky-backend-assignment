package kr.co.teamsky.study.infra.persistence.entity;

import jakarta.persistence.*;
import kr.co.teamsky.study.domain.model.Choice;
import kr.co.teamsky.study.domain.model.id.ChoiceId;
import kr.co.teamsky.study.domain.model.id.ProblemId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "choice")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChoiceEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long problemId;

    @Column(nullable = false)
    private int choiceNumber;

    @Column(nullable = false)
    private String content;

    public Choice toDomain() {
        return new Choice(new ChoiceId(id), new ProblemId(problemId), choiceNumber, content);
    }
}
