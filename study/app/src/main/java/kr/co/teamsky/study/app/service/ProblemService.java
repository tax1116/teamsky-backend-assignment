package kr.co.teamsky.study.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import kr.co.teamsky.study.app.dto.ProblemHistoryResult;
import kr.co.teamsky.study.app.dto.ProblemResult;
import kr.co.teamsky.study.app.dto.SubmitResult;
import kr.co.teamsky.study.app.exception.DuplicateSubmissionException;
import kr.co.teamsky.study.app.exception.InvalidAnswerRequestException;
import kr.co.teamsky.study.app.exception.NoProblemAvailableException;
import kr.co.teamsky.study.app.exception.ProblemNotFoundException;
import kr.co.teamsky.study.app.exception.SubmissionNotFoundException;
import kr.co.teamsky.study.app.exception.UserNotFoundException;
import kr.co.teamsky.study.domain.exception.DuplicateSubmissionConflictException;
import kr.co.teamsky.study.domain.model.*;
import kr.co.teamsky.study.domain.model.id.ChapterId;
import kr.co.teamsky.study.domain.model.id.ProblemId;
import kr.co.teamsky.study.domain.model.id.UserId;
import kr.co.teamsky.study.domain.repository.AnswerRepository;
import kr.co.teamsky.study.domain.repository.ChoiceRepository;
import kr.co.teamsky.study.domain.repository.CorrectRateRepository;
import kr.co.teamsky.study.domain.repository.ProblemRepository;
import kr.co.teamsky.study.domain.repository.SkipCacheRepository;
import kr.co.teamsky.study.domain.repository.SubmissionRepository;
import kr.co.teamsky.study.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProblemService {

    private final ProblemRepository problemRepository;
    private final ChoiceRepository choiceRepository;
    private final AnswerRepository answerRepository;
    private final SubmissionRepository submissionRepository;
    private final SkipCacheRepository skipCacheRepository;
    private final CorrectRateRepository correctRateRepository;
    private final UserRepository userRepository;

    @Transactional
    public ProblemResult getRandomProblem(ChapterId chapterId, UserId userId) {
        validateUserExists(userId);
        List<ProblemId> unsolvedIds = problemRepository.findUnsolvedProblemIds(chapterId, userId);
        ProblemId skippedId = skipCacheRepository.getSkippedProblemId(userId, chapterId);
        log.info(
                "랜덤 문제 조회 시작 - userId={}, chapterId={}, unsolvedCount={}, skippedProblemId={}",
                userId.value(),
                chapterId.value(),
                unsolvedIds.size(),
                skippedId != null ? skippedId.value() : null);

        List<ProblemId> candidates = new ArrayList<>(unsolvedIds);
        if (skippedId != null) {
            candidates.remove(skippedId);
        }

        if (candidates.isEmpty()) {
            log.info("랜덤 문제 조회 결과 없음 - userId={}, chapterId={}, 이유=제공 가능한 문제가 없음", userId.value(), chapterId.value());
            throw new NoProblemAvailableException();
        }

        ProblemId selectedId = candidates.get(ThreadLocalRandom.current().nextInt(candidates.size()));
        if (skippedId != null) {
            skipCacheRepository.clearSkippedProblemId(userId, chapterId);
        }
        log.info(
                "랜덤 문제 조회 완료 - userId={}, chapterId={}, candidateCount={}, selectedProblemId={}",
                userId.value(),
                chapterId.value(),
                candidates.size(),
                selectedId.value());
        return getProblemResult(selectedId);
    }

    @Transactional
    public ProblemResult skipProblem(ChapterId chapterId, UserId userId, ProblemId problemId) {
        validateUserExists(userId);
        Problem problem = problemRepository
                .findById(problemId)
                .orElseThrow(() -> new ProblemNotFoundException(problemId.value()));
        validateChapterOwnership(chapterId, problem);
        skipCacheRepository.setSkippedProblemId(userId, chapterId, problemId);
        log.info(
                "문제 넘기기 처리 - userId={}, chapterId={}, skippedProblemId={}",
                userId.value(),
                chapterId.value(),
                problemId.value());
        return getRandomProblem(chapterId, userId);
    }

    @Transactional
    public SubmitResult submitAnswer(
            ProblemId problemId, UserId userId, ProblemType submittedProblemType, List<String> userAnswers) {
        validateUserExists(userId);
        log.info(
                "문제 제출 시작 - userId={}, problemId={}, answerType={}, userAnswers={}",
                userId.value(),
                problemId.value(),
                submittedProblemType,
                userAnswers);

        Problem problem = problemRepository
                .findById(problemId)
                .orElseThrow(() -> new ProblemNotFoundException(problemId.value()));

        submissionRepository.findByUserIdAndProblemId(userId, problemId).ifPresent(s -> {
            log.info("문제 제출 중복 요청 - userId={}, problemId={}", userId.value(), problemId.value());
            throw new DuplicateSubmissionException(userId.value(), problemId.value());
        });

        validateAnswerRequest(problem, submittedProblemType, userAnswers);

        List<Answer> answers = answerRepository.findByProblemId(problemId);
        List<String> correctAnswers = answers.stream().map(Answer::answer).toList();

        AnswerStatus status = problem.grade(userAnswers, correctAnswers);

        Submission submission = new Submission(null, userId, problemId, status, userAnswers);
        try {
            submissionRepository.save(submission);
        } catch (DuplicateSubmissionConflictException e) {
            log.warn("문제 제출 충돌 - userId={}, problemId={}, 이유=동시성 중복 제출", e.userId(), e.problemId());
            throw new DuplicateSubmissionException(e.userId(), e.problemId(), e);
        }

        boolean isCorrect = status == AnswerStatus.CORRECT;
        correctRateRepository.increment(problemId, isCorrect);

        log.info("문제 제출 완료 - userId={}, problemId={}, answerStatus={}", userId.value(), problemId.value(), status);

        return new SubmitResult(problemId.value(), status, problem.explanation(), correctAnswers);
    }

    private void validateAnswerRequest(Problem problem, ProblemType submittedProblemType, List<String> userAnswers) {
        if (problem.problemType() != submittedProblemType) {
            log.debug(
                    "답안 검증 실패 - problemId={}, problemType={}, submittedType={}, userAnswers={}",
                    problem.id().value(),
                    problem.problemType(),
                    submittedProblemType,
                    userAnswers);
            throw new InvalidAnswerRequestException(
                    "problemType=" + problem.problemType() + ", answerType=" + submittedProblemType);
        }

        if (submittedProblemType == ProblemType.SUBJECTIVE && userAnswers.size() != 1) {
            log.debug(
                    "답안 검증 실패 - problemId={}, answerType=SUBJECTIVE, 이유=답안 개수 오류, userAnswers={}",
                    problem.id().value(),
                    userAnswers);
            throw new InvalidAnswerRequestException("주관식 답안은 1개만 제출할 수 있습니다.");
        }

        if (submittedProblemType == ProblemType.OBJECTIVE) {
            validateObjectiveAnswers(problem.id(), userAnswers);
        }
    }

    private void validateObjectiveAnswers(ProblemId problemId, List<String> userAnswers) {
        List<Choice> choices = choiceRepository.findByProblemId(problemId);
        int maxChoiceNumber = choices.stream()
                .mapToInt(Choice::choiceNumber)
                .max()
                .orElseThrow(() -> new InvalidAnswerRequestException("객관식 선택지가 존재하지 않습니다."));

        for (String userAnswer : userAnswers) {
            int choiceNumber;
            try {
                choiceNumber = Integer.parseInt(userAnswer);
            } catch (NumberFormatException e) {
                log.debug(
                        "답안 검증 실패 - problemId={}, answerType=OBJECTIVE, 이유=숫자 아님, userAnswer={}",
                        problemId.value(),
                        userAnswer);
                throw new InvalidAnswerRequestException("객관식 답안은 1-base 선택지 번호여야 합니다.");
            }

            if (choiceNumber < 1 || choiceNumber > maxChoiceNumber) {
                log.debug(
                        "답안 검증 실패 - problemId={}, answerType=OBJECTIVE, 이유=범위 초과, userAnswer={}, maxChoiceNumber={}",
                        problemId.value(),
                        choiceNumber,
                        maxChoiceNumber);
                throw new InvalidAnswerRequestException("객관식 답안은 1부터 " + maxChoiceNumber + " 사이여야 합니다.");
            }
        }
    }

    @Transactional(readOnly = true)
    public ProblemHistoryResult getProblemHistory(UserId userId, ProblemId problemId) {
        validateUserExists(userId);
        log.info("풀이 이력 조회 시작 - userId={}, problemId={}", userId.value(), problemId.value());
        Submission submission = submissionRepository
                .findByUserIdAndProblemId(userId, problemId)
                .orElseThrow(() -> new SubmissionNotFoundException(userId.value(), problemId.value()));

        Problem problem = problemRepository
                .findById(problemId)
                .orElseThrow(() -> new ProblemNotFoundException(problemId.value()));

        List<Answer> answers = answerRepository.findByProblemId(problemId);
        List<String> correctAnswers = answers.stream().map(Answer::answer).toList();

        ProblemHistoryResult result = new ProblemHistoryResult(
                problemId.value(),
                submission.answerStatus(),
                problem.explanation(),
                correctAnswers,
                submission.userAnswers(),
                calculateCorrectRate(problemId));
        log.info(
                "풀이 이력 조회 완료 - userId={}, problemId={}, answerStatus={}, answerCorrectRate={}",
                userId.value(),
                problemId.value(),
                result.answerStatus(),
                result.answerCorrectRate());
        return result;
    }

    private ProblemResult getProblemResult(ProblemId problemId) {
        Problem problem = problemRepository
                .findById(problemId)
                .orElseThrow(() -> new ProblemNotFoundException(problemId.value()));
        List<Choice> choices = choiceRepository.findByProblemId(problemId);
        return ProblemResult.of(problem, choices, calculateCorrectRate(problemId));
    }

    private Integer calculateCorrectRate(ProblemId problemId) {
        return correctRateRepository
                .findStats(problemId)
                .map(CorrectRateStats::calculateCorrectRate)
                .orElse(null);
    }

    private void validateUserExists(UserId userId) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId.value()));
    }

    private void validateChapterOwnership(ChapterId chapterId, Problem problem) {
        if (!problem.chapterId().equals(chapterId)) {
            log.warn(
                    "문제 챕터 검증 실패 - chapterId={}, problemId={}, actualChapterId={}",
                    chapterId.value(),
                    problem.id().value(),
                    problem.chapterId().value());
            throw new ProblemNotFoundException(problem.id().value());
        }
    }
}
