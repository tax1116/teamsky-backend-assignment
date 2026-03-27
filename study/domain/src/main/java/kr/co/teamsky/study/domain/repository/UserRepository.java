package kr.co.teamsky.study.domain.repository;

import java.util.Optional;
import kr.co.teamsky.study.domain.model.User;
import kr.co.teamsky.study.domain.model.id.UserId;

public interface UserRepository {

    Optional<User> findById(UserId userId);
}
