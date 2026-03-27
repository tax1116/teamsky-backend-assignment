package kr.co.teamsky.study.infra.persistence.repository;

import java.util.List;
import java.util.Optional;
import kr.co.teamsky.study.infra.persistence.entity.SubmissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaSubmissionRepository extends JpaRepository<SubmissionEntity, Long> {

    Optional<SubmissionEntity> findByUserIdAndProblemId(Long userId, Long problemId);

    @Query(
            """
            SELECT s.problemId, COUNT(s), SUM(CASE WHEN s.answerStatus = 'CORRECT' THEN 1 ELSE 0 END)
            FROM SubmissionEntity s
            GROUP BY s.problemId
            """)
    List<Object[]> aggregateCorrectRate();
}
