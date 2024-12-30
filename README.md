# 김영한님의 Querydsl 강의 정리
<br>
<hr>
<br>

## ✔️ querydsl directory
### 학습 범위 : 5-1-1 - 5-1-5
- querydsl project 생성
  - springboot 3.x gradle 설정
<br>

- application.yml 셋팅
<br>
<br>

### 학습 범위 : 5-2-1 - 5-3-12
- 예제 도메인 세팅

**➡️ JPQL vs Querydsl**
  - JPQL : 수동 파라미터 바인딩, SQL 문법 오류를 런타임 시점에 확인 가능
  - Querydsl : 자동 파라미터 바인딩, SQL 문법 오류를 컴파일 시점에 확인 가능<br>
               querydsl은 결국 jpql로 변환되서 실행이 된다.
<br>

**➡️ QType**
- querydsl은 기존 엔티티에 Q라는 명칭을 붙여 새로운 엔티티를 생성한다.
  - Member > QMember 생성됨
<br>

- `new QMember("별칭")` : 엔티티 인스턴스 생성 시 별칭을 정해줘야 하는데, 처음 조회하는 값으로 자동 세팅이 된다.
  - 해당 별칭을 통해 querydsl을 사용한다.
  - 만약 동일 테이블을 join하여 사용할 경우 별칭을 다르게 해야되므로, 각각 QMember를 생성해야 한다.
<br>

**➡️ 검색 조건**

```java
member.username.eq("member1"); // username == 'member1'
member.username.ne("member1"); // username != 'member1'
member.username.eq("member1").not() // username != 'member1'

member.username.isNotNull() // 이름이 is not null

member.age.in(10,20) // age가 10 또는 20
member.age.notIn(10, 20) // age가 10과 20이 아닌 것
member.age.between(10, 20) // age가 10-20 사이

member.age.goe(30) // age >= 30
member.age.gt(30) // age > 30
member.age.loe(30) // age <= 30
member.age.lt(30) // age < 30

member.username.like("member%") // like 검색
member.username.contains("%member%") // like '%member%' 검색
member.username.startsWIth("member") // like 'member%' 검색
```
<br>

➡️ **결과 조회**
- fetch

- fetchOne

- fetchFrist

- fetchResults

- fetchCount
<br>

**➡️ 정렬**
- orderBy : desc, asc
<br>

➡️ **페이징**
- fetchResults를 이용한 데이터 추출
  - total
  - offset
  - limit
<br>

**➡️ 집합**
- count, sum, age, avg, max, min

- join + group by
<br>

**➡️ 조인**
- 기본 조인

- theta join : cardisian 곱을 통해 연관 없는 테이블끼리의 값을 전부 출력 가능

- 일반 조인 : `leftJoin(member.team, team)` -> 엔티티의 id 끼리 1차적으로 on절 비교를 함
  - 추가로 on절 사용 가능
  - 해당 문법은 콘솔에 출력되는 쿼리를 보면 조인 이후에 member.team.id와 team.id를 on절로 묶어준다.<br>
    즉, join 안에 엔티티를 2개 삽입할 경우, ID 값을 자동으로 on절로 묶어주는 것
<br>

- on 조인 : `from(member).leftJoin(team).on(xxx)` -> 일반적으로 sql에서 쓰는 조인 느낌이며, join에 사용할 엔티티(테이블)을 1개만 삽입

- fetch 조인 : LAZY 로딩에 대한 성능 최적화
<br>

**➡️ 서브 쿼리**
- JPAExpressions 사용

- 같은 member 엔티티 사용 시 QType 별칭이 겹치기 때문에 새로운 QType 인스턴스를 생성해야 한다.

- 💡 from 절에서의 서브 쿼리는 querydsl로도 지원하지 않기 때문에 아래와 같은 방안을 모색해야 한다.
  - 서브쿼리를 join 으로 변경
  - 애플리케이션에서 쿼리를 2번 분리해서 실행
  - naviteSQL 사용
<br>

**➡️ case**
- case when then

- `new CaseBuilder()` 사용 시 복잡한 case문법 사용이 가능
  - between 같은 함수는 caseBuilder를 사용하지 않으면 불가능
<br>

**➡️ 상수, 문자 더하기**
- `Expressions.constant`

- concat

- stringValue() : 문자로 cast 해주는 함수 -> ENUM 타입 사용 시 해당 함수로 변환해야 함
<br>
<br>

### 학습 범위 : 5-4-1 - 5-4-7
**➡️ 프로젝션**
- 대상이 하나 일 때 -> 단순 조회

- 대상이 둘 이상일 때 -> Tuple 타입을 사용
  - 그러나 Tuple은 querydsl에 종속적인 타입이므로 비즈니스 로직에서는 사용해서는 안된다.
  - 이를 dto로 변환하여 사용하는 것이 권장
 
<br>

**➡️ 프로젝션 - dto로 변환**
- JPQL : 생성자 방식만 지원하기 때문에 dto에 생성자가 있어야 한다.
 - `em.createQuery("select new study.querydsl.entity.dto.MemberDto(m.username, m.age) from Member m", MemberDto.class)`
 - 패키지명 경로 맵핑이 필수 -> new Operation 방식
<br>

- querydsl : Projections 유틸 사용
  - Projections.bean : setter 접근법
  - Projections.field : setter를 사용하지 않고 field에 바로 값을 꽂음
    - field와 querydsl 타입이 다르면 값이 null로 출력 -> as 문법으로 해결 가능
    - `member.username.as("name")`
  - Projections.constructor : 생성자를 통해 주입하므로 생성자에 등록된 타입과 querydsl에 사용하는 타입이 일치해야 한다
    - 생성자와 맞지 않는 데이터가 querydsl에 선언된 경우 컴파일 시점에 에러를 확인할 수 없는 단점이 있다.
<br>

**➡️ 프로젝션 - @QueryProjection**
- MemberDto 생성자에 어노테이션으로 선언하여 사용

- Projections.constructor와 다르게 생성자와 데이터가 맞지 않으면 컴파일 시점에 에러를 발견해준다.

- 그러나 MemberDto에 `@QueryProjection`이라는 querydsl의 라이브러리 의존성이 생겨버린다.
  - MemberDto는 서비스, 컨트롤러 등 다양한 레이어에서 사용가능 하기 때문에 하부 기술인 querydsl에 의존하는 것은 좋지 않다.
<br>

**➡️ 동적 쿼리**
- `BooleanBuilder builder = new BooleanBuilder(초기값 설정 가능)`
  - builder.and(member.username.eq(param))
  - 위와 같은 문법을 통해 builder에 쿼리를 동적으로 추가
  - where(builder) : where문에 builder 변수를 넣어서 동적으로 실행되게 함
<br>

- 동적 where
  - where 문 안에 메서드를 직접 호출하는 형식

```java
    @Test
    public void dynamicQuery_WhereParam() {
        String usernameParam = "member1";
        Integer ageParam = 10;

        List<Member> result = searchMember2(usernameParam, ageParam);
        assertThat(result.size()).isEqualTo(1);
    }

    private List<Member> searchMember2(String usernameCond, Integer ageCond) {
        return queryFactory
                .selectFrom(member)
                .where(usernameEq(usernameCond), ageEq(ageCond))
                .fetch();
    }
    private BooleanExpression usernameEq(String usernameCond) {
        // 삼항 연산 -> return usernameCond != null ? member.username.eq(usernameCond) : null;
        if (usernameCond == null) {
            return null;
        }
        return member.username.eq(usernameCond);
    }
    private BooleanExpression ageEq(Integer ageCond) {
        if (ageCond == null) {
            return null;
        }
        return member.age.eq(ageCond);
    }
```
<br>

- 생성한 메서드는 Predicate 또는 BooleanExpression 타입으로 선언 가능
  - BooleanExpression으로 해야 다른 메서드에서 추가적인 조립이 가능하다.
  - 그러므로 Prediate 대신 BOoleanExpression으로 선언하는 것을 권장
<br>

**➡️ 벌크 연산**
- update, delete

- 벌크 연산의 경우 영속성 컨텍스트를 무시하고 DB에 바로 반영된다.
  - 즉, 벌크 연산 이후 조회를 해보면 영속성 컨텍스트와 DB의 데이터가 상이
  - 해결법은 벌크 연산 후 `em.flush(), em.clear()`를 실행
<br>

- 더하기 : add()
- 곱하기 : multiply()
<br>

**➡️ SQL Function 호출하기**
- querydsl에서 sql 자체에서 사용하는 function을 호출하여 사용이 가능하다.

replace
```java
queryFactory
  .select(Expressions.stringTemplate(
    "function('replace', {0}, {1}, {2})",
    member.username, "member, "M"))
  .from(member)
  .fetch();
```
> member.username에서 member라는 데이터를 M으로 변경한다.
<br>

lower
```java
queryFactory
  .select(member.username)
  .from(member)
  .where(member.username.eq(Expressions.stringTemplate(
          "function('lower', {0})",
          member.username)))
  .fetch();
```
<br>

- 그러나 replce, lower와 같은 기본 함수 대부분이 querydsl에서도 제공한다. -> `.where(member.username.eq(member.username.lower())`

- sql 내장 함수 필요 시 querydsl에 있는지 확인하고 없다면은 template을 사용한다.

- yml 설정을 통해 DB 자체를 상속받아 직접 정의하는 방법도 존재한다.
<br>
<br>

### 학습 범위 : 5-5-1 - 5-5-4
**➡️ 순수 JPA와 querydsl의 점진적 비교 및 테스트**
- 실제 repository에서 querydsl을 작성

- repository -> BooleanBuilder를 사용한 동적쿼리 작성

- repository -> where에 메서드를 사용한 동적쿼리 작성

- controller -> querydsl 로직 실행
<br>
<br>

### 학습 범위 : 5-6-1- - 5-6-5
**➡️ Jparepository > SpringDataJpaRepository로 변경**
- Pageable 적용

- 
