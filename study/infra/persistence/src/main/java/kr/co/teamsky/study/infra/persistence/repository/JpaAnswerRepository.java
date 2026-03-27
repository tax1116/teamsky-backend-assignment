package kr.co.teamsky.study.infra.persistence.repository;

import java.util.List;
import kr.co.teamsky.study.infra.persistence.entity.AnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAnswerRepository extends JpaRepository<AnswerEntity, Long> {

    List<AnswerEntity> findByProblemId(Long problemId);
}
