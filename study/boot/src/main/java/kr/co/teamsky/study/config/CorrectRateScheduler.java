package kr.co.teamsky.study.config;

import kr.co.teamsky.study.infra.cache.InMemoryCorrectRateRepository;
import kr.co.teamsky.study.infra.persistence.repository.JpaSubmissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CorrectRateScheduler {

    private final JpaSubmissionRepository jpaSubmissionRepository;
    private final InMemoryCorrectRateRepository correctRateRepository;

    @Scheduled(initialDelay = 0, fixedRateString = "${correct-rate.sync-interval-ms:300000}")
    public void syncCorrectRate() {
        var aggregates = jpaSubmissionRepository.aggregateCorrectRate();
        for (Object[] row : aggregates) {
            Long problemId = (Long) row[0];
            int solveCount = ((Number) row[1]).intValue();
            int correctCount = ((Number) row[2]).intValue();
            correctRateRepository.load(problemId, solveCount, correctCount);
        }
        log.info("정답률 보정 완료: {}건", aggregates.size());
    }
}
