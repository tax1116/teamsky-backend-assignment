package kr.co.teamsky.study.domain.repository;

import java.util.List;
import kr.co.teamsky.study.domain.model.Answer;
import kr.co.teamsky.study.domain.model.id.ProblemId;

public interface AnswerRepository {

    List<Answer> findByProblemId(ProblemId problemId);
}
