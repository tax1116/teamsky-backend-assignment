package kr.co.teamsky.study.infra.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.time.Duration;
import kr.co.teamsky.study.domain.model.id.ChapterId;
import kr.co.teamsky.study.domain.model.id.ProblemId;
import kr.co.teamsky.study.domain.model.id.UserId;
import kr.co.teamsky.study.domain.repository.SkipCacheRepository;
import org.springframework.stereotype.Component;

@Component
public class CaffeineSkipCacheRepository implements SkipCacheRepository {

    private final Cache<String, Long> cache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofMinutes(30))
            .maximumSize(10_000)
            .build();

    @Override
    public ProblemId getSkippedProblemId(UserId userId, ChapterId chapterId) {
        Long value = cache.getIfPresent(buildKey(userId, chapterId));
        return value != null ? new ProblemId(value) : null;
    }

    @Override
    public void setSkippedProblemId(UserId userId, ChapterId chapterId, ProblemId problemId) {
        cache.put(buildKey(userId, chapterId), problemId.value());
    }

    @Override
    public void clearSkippedProblemId(UserId userId, ChapterId chapterId) {
        cache.invalidate(buildKey(userId, chapterId));
    }

    private String buildKey(UserId userId, ChapterId chapterId) {
        return userId.value() + ":" + chapterId.value();
    }
}
