package kr.co.teamsky.study.infra.persistence.entity;

import jakarta.persistence.*;
import kr.co.teamsky.study.domain.model.User;
import kr.co.teamsky.study.domain.model.id.UserId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    public User toDomain() {
        return new User(new UserId(id), name);
    }
}
