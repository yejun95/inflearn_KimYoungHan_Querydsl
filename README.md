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

### 학습 범위 : 5-2-1 - 
- 예제 도메인 세팅

➡️ **JPQL vs Querydsl**
  - JPQL : 수동 파라미터 바인딩, SQL 문법 오류를 런타임 시점에 확인 가능
  - Querydsl : 자동 파라미터 바인딩, SQL 문법 오류를 컴파일 시점에 확인 가능<br>
               querydsl은 결국 jpql로 변환되서 실행이 된다.
<br>

➡️ **QType**
- querydsl은 기존 엔티티에 Q라는 명칭을 붙여 새로운 엔티티를 생성한다.
  - Member > QMember 생성됨
<br>

- `new QMember("별칭")` : 엔티티 인스턴스 생성 시 별칭을 정해줘야 하는데, 처음 조회하는 값으로 자동 세팅이 된다.
  - 해당 별칭을 통해 querydsl을 사용한다.
  - 만약 동일 테이블을 join하여 사용할 경우 별칭을 다르게 해야되므로, 각각 QMember를 생성해야 한다.
<br>

➡️ **검색 조건**

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
