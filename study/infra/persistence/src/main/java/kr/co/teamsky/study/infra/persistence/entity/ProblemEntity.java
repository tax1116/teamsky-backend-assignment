package kr.co.teamsky.study.infra.persistence.entity;

import jakarta.persistence.*;
import kr.co.teamsky.study.domain.model.Problem;
import kr.co.teamsky.study.domain.model.ProblemType;
import kr.co.teamsky.study.domain.model.id.ChapterId;
import kr.co.teamsky.study.domain.model.id.ProblemId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "problem")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProblemEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long chapterId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String explanation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProblemType problemType;

    public Problem toDomain() {
        return new Problem(new ProblemId(id), new ChapterId(chapterId), content, explanation, problemType);
    }
}
