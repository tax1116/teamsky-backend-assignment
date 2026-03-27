package kr.co.teamsky.study.infra.persistence.repository;

import java.util.Optional;
import kr.co.teamsky.study.domain.exception.DuplicateSubmissionConflictException;
import kr.co.teamsky.study.domain.model.Submission;
import kr.co.teamsky.study.domain.model.id.ProblemId;
import kr.co.teamsky.study.domain.model.id.UserId;
import kr.co.teamsky.study.domain.repository.SubmissionRepository;
import kr.co.teamsky.study.infra.persistence.entity.SubmissionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SubmissionRepositoryImpl implements SubmissionRepository {

    private final JpaSubmissionRepository jpaSubmissionRepository;

    @Override
    public Submission save(Submission submission) {
        SubmissionEntity entity = SubmissionEntity.from(submission);
        try {
            return jpaSubmissionRepository.save(entity).toDomain();
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateSubmissionConflictException(
                    submission.userId().value(), submission.problemId().value(), e);
        }
    }

    @Override
    public Optional<Submission> findByUserIdAndProblemId(UserId userId, ProblemId problemId) {
        return jpaSubmissionRepository
                .findByUserIdAndProblemId(userId.value(), problemId.value())
                .map(SubmissionEntity::toDomain);
    }
}
