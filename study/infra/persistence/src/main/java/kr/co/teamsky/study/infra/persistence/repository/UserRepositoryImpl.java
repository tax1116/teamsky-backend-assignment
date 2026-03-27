package kr.co.teamsky.study.infra.persistence.repository;

import java.util.Optional;
import kr.co.teamsky.study.domain.model.User;
import kr.co.teamsky.study.domain.model.id.UserId;
import kr.co.teamsky.study.domain.repository.UserRepository;
import kr.co.teamsky.study.infra.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    @Override
    public Optional<User> findById(UserId userId) {
        return jpaUserRepository.findById(userId.value()).map(UserEntity::toDomain);
    }
}
