package kr.co.teamsky.study.domain.repository;

import java.util.Optional;
import kr.co.teamsky.study.domain.model.Submission;
import kr.co.teamsky.study.domain.model.id.ProblemId;
import kr.co.teamsky.study.domain.model.id.UserId;

public interface SubmissionRepository {

    Submission save(Submission submission);

    Optional<Submission> findByUserIdAndProblemId(UserId userId, ProblemId problemId);
}
