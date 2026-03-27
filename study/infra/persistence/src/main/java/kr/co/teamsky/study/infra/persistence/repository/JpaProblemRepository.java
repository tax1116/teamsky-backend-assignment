package kr.co.teamsky.study.infra.persistence.repository;

import java.util.List;
import kr.co.teamsky.study.infra.persistence.entity.ProblemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaProblemRepository extends JpaRepository<ProblemEntity, Long> {

    @Query(
            """
            SELECT p.id FROM ProblemEntity p
            WHERE p.chapterId = :chapterId
            AND p.id NOT IN (
                SELECT s.problemId FROM SubmissionEntity s WHERE s.userId = :userId
            )
            """)
    List<Long> findUnsolvedProblemIds(Long chapterId, Long userId);
}
