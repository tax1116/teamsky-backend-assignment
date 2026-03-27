package kr.co.teamsky.study.domain.repository;

import java.util.List;
import java.util.Optional;
import kr.co.teamsky.study.domain.model.Problem;
import kr.co.teamsky.study.domain.model.id.ChapterId;
import kr.co.teamsky.study.domain.model.id.ProblemId;
import kr.co.teamsky.study.domain.model.id.UserId;

public interface ProblemRepository {

    Optional<Problem> findById(ProblemId id);

    List<ProblemId> findUnsolvedProblemIds(ChapterId chapterId, UserId userId);
}
