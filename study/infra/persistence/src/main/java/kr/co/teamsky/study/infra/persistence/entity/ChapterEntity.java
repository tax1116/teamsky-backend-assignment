package kr.co.teamsky.study.infra.persistence.entity;

import jakarta.persistence.*;
import kr.co.teamsky.study.domain.model.Chapter;
import kr.co.teamsky.study.domain.model.id.ChapterId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chapter")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChapterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    public Chapter toDomain() {
        return new Chapter(new ChapterId(id), name);
    }
}
