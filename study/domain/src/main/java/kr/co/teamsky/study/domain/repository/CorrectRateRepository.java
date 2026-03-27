package kr.co.teamsky.study.domain.repository;

import kr.co.teamsky.study.domain.model.id.ProblemId;

public interface CorrectRateRepository {

    void increment(ProblemId problemId, boolean isCorrect);

    Integer calculateCorrectRate(ProblemId problemId);
}
