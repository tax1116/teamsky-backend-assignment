package kr.co.teamsky.study.infra.persistence.repository;

import java.util.List;
import java.util.Optional;
import kr.co.teamsky.study.domain.model.Problem;
import kr.co.teamsky.study.domain.model.id.ChapterId;
import kr.co.teamsky.study.domain.model.id.ProblemId;
import kr.co.teamsky.study.domain.model.id.UserId;
import kr.co.teamsky.study.domain.repository.ProblemRepository;
import kr.co.teamsky.study.infra.persistence.entity.ProblemEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProblemRepositoryImpl implements ProblemRepository {

    private final JpaProblemRepository jpaProblemRepository;

    @Override
    public Optional<Problem> findById(ProblemId id) {
        return jpaProblemRepository.findById(id.value()).map(ProblemEntity::toDomain);
    }

    @Override
    public List<ProblemId> findUnsolvedProblemIds(ChapterId chapterId, UserId userId) {
        return jpaProblemRepository.findUnsolvedProblemIds(chapterId.value(), userId.value()).stream()
                .map(ProblemId::new)
                .toList();
    }
}
