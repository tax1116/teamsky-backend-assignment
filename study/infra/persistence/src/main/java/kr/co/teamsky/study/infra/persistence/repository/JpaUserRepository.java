package kr.co.teamsky.study.infra.persistence.repository;

import kr.co.teamsky.study.infra.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {}
