package kr.co.teamsky.study.domain.repository;

import kr.co.teamsky.study.domain.model.id.ChapterId;
import kr.co.teamsky.study.domain.model.id.ProblemId;
import kr.co.teamsky.study.domain.model.id.UserId;

public interface SkipCacheRepository {

    ProblemId getSkippedProblemId(UserId userId, ChapterId chapterId);

    void setSkippedProblemId(UserId userId, ChapterId chapterId, ProblemId problemId);

    void clearSkippedProblemId(UserId userId, ChapterId chapterId);
}
