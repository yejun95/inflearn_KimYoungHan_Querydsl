package study.querydsl;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.Team;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static study.querydsl.entity.QMember.*;

@SpringBootTest
@Transactional
public class QueryDslBasicTest {

    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em);
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);

        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
    }

    //JPQL을 이용해 member1을 찾기
    @Test
    public void startJPQL() {
        Member findMember = em.createQuery("select m from Member m where m.username = :username", Member.class)
                .setParameter("username", "member1")
                .getSingleResult();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    //querydsl 이용해 member1을 찾기
    @Test
    public void startQuerydsl() {
        Member findMember = queryFactory
                .select(member) // QMember.member로 사용하는 것을 static import를 통해 코드를 줄인다.
                .from(member)
                .where(member.username.eq("member1")) //자동으로 파라미터 바인딩이 된다.
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    //검색 조건
    @Test
    public void search() {
        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1")
                        .and(member.age.eq(10)))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void searchAndParam() {
        Member findMember = queryFactory
                .selectFrom(member)
                .where(
                        member.username.eq("member1"),
                        member.age.between(10, 30) //and의 쉼표 사용 방식
                )
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    //결과 조회
    @Test
    public void resultFetch() {
        // 리스트 조회 : fetch()
        List<Member> fetch = queryFactory
                .selectFrom(member)
                .fetch();

        // 단건 조회 : fetchOne()
        // 결과가 없으면 null, 결과가 둘 이상이면 NonUniqueResultException 반환
        Member member = queryFactory
                .selectFrom(QMember.member)
                .fetchOne();

        // 처음 1건만 조회 : fetchFirst() > limit(1).fetchOne()을 실행하는 것과 같음
        Member member1 = queryFactory
                .selectFrom(QMember.member)
                .fetchFirst();

        // 페이징에서 사용
        QueryResults<Member> results = queryFactory
                .selectFrom(QMember.member)
                .fetchResults();

        results.getTotal();
        results.getLimit();
        results.getOffset();

        // 카운트 쿼리
        long total = queryFactory
                .selectFrom(QMember.member)
                .fetchCount();
    }
}
