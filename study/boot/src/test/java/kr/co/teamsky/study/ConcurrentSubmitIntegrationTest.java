package kr.co.teamsky.study;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import kr.co.teamsky.study.api.dto.request.SubmitAnswerRequest;
import kr.co.teamsky.study.api.dto.request.SubmitAnswerRequest.UserAnswer;
import kr.co.teamsky.study.api.dto.response.SubmitResultResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ConcurrentSubmitIntegrationTest {

    // userId 6~10: 동시 제출 전용
    private static final long CONCURRENT_USER_START = 6L;

    @LocalServerPort
    private int port;

    private RestClient restClient;

    @BeforeEach
    void setUp() {
        restClient = RestClient.builder().baseUrl("http://localhost:" + port).build();
    }

    @Test
    void 여러_유저가_같은_문제에_동시_제출하면_모두_성공한다() throws Exception {
        int threadCount = 5;
        Long problemId = 1L;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(1);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);
        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            long userId = CONCURRENT_USER_START + i;
            futures.add(executor.submit(() -> {
                try {
                    latch.await();
                    var response = restClient
                            .post()
                            .uri("/api/problems/submit")
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(new SubmitAnswerRequest(problemId, userId, new UserAnswer("OBJECTIVE", List.of("1"))))
                            .retrieve()
                            .body(SubmitResultResponse.class);

                    if (response != null) {
                        successCount.incrementAndGet();
                    }
                } catch (Exception e) {
                    System.err.println("userId=" + userId + " failed: " + e.getMessage());
                    failCount.incrementAndGet();
                }
            }));
        }

        latch.countDown();

        for (Future<?> future : futures) {
            future.get();
        }

        executor.shutdown();

        assertThat(successCount.get()).isEqualTo(threadCount);
        assertThat(failCount.get()).isZero();
    }
}
