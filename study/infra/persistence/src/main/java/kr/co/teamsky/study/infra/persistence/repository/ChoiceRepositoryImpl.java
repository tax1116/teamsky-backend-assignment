package kr.co.teamsky.study.infra.persistence.repository;

import java.util.List;
import kr.co.teamsky.study.domain.model.Choice;
import kr.co.teamsky.study.domain.model.id.ProblemId;
import kr.co.teamsky.study.domain.repository.ChoiceRepository;
import kr.co.teamsky.study.infra.persistence.entity.ChoiceEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChoiceRepositoryImpl implements ChoiceRepository {

    private final JpaChoiceRepository jpaChoiceRepository;

    @Override
    public List<Choice> findByProblemId(ProblemId problemId) {
        return jpaChoiceRepository.findByProblemIdOrderByChoiceNumberAsc(problemId.value()).stream()
                .map(ChoiceEntity::toDomain)
                .toList();
    }
}
