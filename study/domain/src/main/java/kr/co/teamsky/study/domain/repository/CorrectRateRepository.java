package kr.co.teamsky.study.domain.repository;

import java.util.Optional;
import kr.co.teamsky.study.domain.model.CorrectRateStats;
import kr.co.teamsky.study.domain.model.id.ProblemId;

public interface CorrectRateRepository {

    void increment(ProblemId problemId, boolean isCorrect);

    Optional<CorrectRateStats> findStats(ProblemId problemId);
}
