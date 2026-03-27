package kr.co.teamsky.study.infra.persistence.repository;

import java.util.List;
import kr.co.teamsky.study.domain.model.Answer;
import kr.co.teamsky.study.domain.model.id.ProblemId;
import kr.co.teamsky.study.domain.repository.AnswerRepository;
import kr.co.teamsky.study.infra.persistence.entity.AnswerEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AnswerRepositoryImpl implements AnswerRepository {

    private final JpaAnswerRepository jpaAnswerRepository;

    @Override
    public List<Answer> findByProblemId(ProblemId problemId) {
        return jpaAnswerRepository.findByProblemId(problemId.value()).stream()
                .map(AnswerEntity::toDomain)
                .toList();
    }
}
