package kr.co.teamsky.study.api.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SkipProblemRequest(
        @NotNull @Positive Long chapterId, @NotNull @Positive Long userId, @NotNull @Positive Long problemId) {}
