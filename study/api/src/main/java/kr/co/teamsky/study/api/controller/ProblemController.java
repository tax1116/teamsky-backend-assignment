package kr.co.teamsky.study.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import kr.co.teamsky.study.api.dto.request.RandomProblemRequest;
import kr.co.teamsky.study.api.dto.request.SkipProblemRequest;
import kr.co.teamsky.study.api.dto.request.SubmitAnswerRequest;
import kr.co.teamsky.study.api.dto.response.ProblemHistoryResponse;
import kr.co.teamsky.study.api.dto.response.ProblemResponse;
import kr.co.teamsky.study.api.dto.response.SubmitResultResponse;
import kr.co.teamsky.study.app.service.ProblemService;
import kr.co.teamsky.study.domain.model.ProblemType;
import kr.co.teamsky.study.domain.model.id.ChapterId;
import kr.co.teamsky.study.domain.model.id.ProblemId;
import kr.co.teamsky.study.domain.model.id.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/api/problems")
@RequiredArgsConstructor
public class ProblemController {

    private final ProblemService problemService;

    @Operation(summary = "랜덤 문제 조회")
    @ApiResponse(responseCode = "200", description = "랜덤 문제 조회 성공")
    @PostMapping("/random")
    public ResponseEntity<ProblemResponse> getRandomProblem(@Valid @RequestBody RandomProblemRequest request) {
        var result = problemService.getRandomProblem(new ChapterId(request.chapterId()), new UserId(request.userId()));
        return ResponseEntity.ok(ProblemResponse.from(result));
    }

    @Operation(summary = "문제 스킵")
    @ApiResponse(responseCode = "200", description = "문제 스킵 성공")
    @PostMapping("/skip")
    public ResponseEntity<ProblemResponse> skipProblem(@Valid @RequestBody SkipProblemRequest request) {
        var result = problemService.skipProblem(
                new ChapterId(request.chapterId()), new UserId(request.userId()), new ProblemId(request.problemId()));
        return ResponseEntity.ok(ProblemResponse.from(result));
    }

    @Operation(summary = "문제 제출")
    @ApiResponse(responseCode = "200", description = "문제 제출 성공")
    @PostMapping("/submit")
    public ResponseEntity<SubmitResultResponse> submitAnswer(@Valid @RequestBody SubmitAnswerRequest request) {
        var result = problemService.submitAnswer(
                new ProblemId(request.problemId()),
                new UserId(request.userId()),
                ProblemType.valueOf(request.userAnswer().answerType()),
                request.userAnswer().answers());
        return ResponseEntity.ok(SubmitResultResponse.from(result));
    }

    @Operation(summary = "풀이 이력 상세 조회")
    @ApiResponse(responseCode = "200", description = "풀이 이력 조회 성공")
    @GetMapping("/history")
    public ResponseEntity<ProblemHistoryResponse> getProblemHistory(
            @RequestParam @Positive Long userId, @RequestParam @Positive Long problemId) {
        var result = problemService.getProblemHistory(new UserId(userId), new ProblemId(problemId));
        return ResponseEntity.ok(ProblemHistoryResponse.from(result));
    }
}
