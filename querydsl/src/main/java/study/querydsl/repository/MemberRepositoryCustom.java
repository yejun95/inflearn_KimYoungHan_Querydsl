package study.querydsl.repository;

import study.querydsl.entity.Member;
import study.querydsl.entity.dto.MemberSearchCondition;
import study.querydsl.entity.dto.MemberTeamDto;

import java.util.List;

public interface MemberRepositoryCustom {
    List<MemberTeamDto> search(MemberSearchCondition condition);
}
