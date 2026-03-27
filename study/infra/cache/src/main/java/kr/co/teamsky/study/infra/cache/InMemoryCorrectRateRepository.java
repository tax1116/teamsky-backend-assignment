package kr.co.teamsky.study.infra.cache;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import kr.co.teamsky.study.domain.model.CorrectRateStats;
import kr.co.teamsky.study.domain.model.id.ProblemId;
import kr.co.teamsky.study.domain.repository.CorrectRateRepository;
import org.springframework.stereotype.Component;

@Component
public class InMemoryCorrectRateRepository implements CorrectRateRepository {

    private final ConcurrentHashMap<Long, AtomicInteger> solveCountMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, AtomicInteger> correctCountMap = new ConcurrentHashMap<>();

    @Override
    public void increment(ProblemId problemId, boolean isCorrect) {
        solveCountMap
                .computeIfAbsent(problemId.value(), k -> new AtomicInteger(0))
                .incrementAndGet();
        if (isCorrect) {
            correctCountMap
                    .computeIfAbsent(problemId.value(), k -> new AtomicInteger(0))
                    .incrementAndGet();
        }
    }

    @Override
    public Optional<CorrectRateStats> findStats(ProblemId problemId) {
        AtomicInteger solveCount = solveCountMap.get(problemId.value());
        if (solveCount == null) {
            return Optional.empty();
        }
        AtomicInteger correctCount = correctCountMap.getOrDefault(problemId.value(), new AtomicInteger(0));
        return Optional.of(new CorrectRateStats(solveCount.get(), correctCount.get()));
    }

    public void load(Long problemId, int solveCount, int correctCount) {
        solveCountMap.put(problemId, new AtomicInteger(solveCount));
        correctCountMap.put(problemId, new AtomicInteger(correctCount));
    }
}
