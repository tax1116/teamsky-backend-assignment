package kr.co.teamsky.study.infra.persistence.repository;

import java.util.List;
import kr.co.teamsky.study.infra.persistence.entity.ChoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaChoiceRepository extends JpaRepository<ChoiceEntity, Long> {

    List<ChoiceEntity> findByProblemIdOrderByChoiceNumberAsc(Long problemId);
}
