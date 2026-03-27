package kr.co.teamsky.study.domain.model;

import kr.co.teamsky.study.domain.model.id.AnswerId;
import kr.co.teamsky.study.domain.model.id.ProblemId;

public record Answer(AnswerId id, ProblemId problemId, String answer) {}
