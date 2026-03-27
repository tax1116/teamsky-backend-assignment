package kr.co.teamsky.study.domain.repository;

import java.util.List;
import kr.co.teamsky.study.domain.model.Choice;
import kr.co.teamsky.study.domain.model.id.ProblemId;

public interface ChoiceRepository {

    List<Choice> findByProblemId(ProblemId problemId);
}
