package jpql;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        // JPA의 모든 데이터 변경은 트랜잭션 안에서 실행해야 한다.
        // EntityManagerFactory는 웹서버가 올라오는 시점에서 DB당 1개만 생성되어 애플리케이션 전체에서 공유한다.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        // EntityManager는 요청이 있을때마다, 생성이되고, 수행 완료되는 시점에서 close 해야 한다. (쓰레드간에 공유 X)
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

/*
            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            TypedQuery<Member> query1 = em.createQuery("select m from Member m", Member.class);
            TypedQuery<String> query2 = em.createQuery("select m.username from Member m", String.class);
            Query query3 = em.createQuery("select m.username, m.age from Member m");
            List<Member> resultList = query1.getResultList();
            for (Member member1 : resultList) {
                System.out.println("member1 = " + member1);
            }
            Member result = query1.getSingleResult(); // 정말 결과가 1개일때만 사용해야한다. 결과가 없거나 2개 이상일떄 모두 Exception이 터진다. Spring Data Jpa에서는 `getSingleResult`가 표준스펙이기 때문에 사용하기는 하지만, 내부적으로 예외처리를 해줘서 Optional을 반환해준다.
            System.out.println("result = " + result);

            tx.commit();
*/

/*
            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);
//            TypedQuery<Member> query4 = em.createQuery("select m from Member m where m.username = :username", Member.class);
//            query4.setParameter("username", "member1");
//            Member singleResult = query4.getSingleResult();
//            System.out.println("singleResult = " + singleResult.getUsername());
            // 체이닝 형식으로 쓰면 편리하다.
            Member result2 = em.createQuery("select m from Member m where m.username = :username", Member.class)
                    .setParameter("username", "member1")
                    .getSingleResult();
            System.out.println("singleResult = " + result2.getUsername());

            tx.commit();
*/

/*
            // TODO : 프로젝션(SELECT)
            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            em.flush();
            em.clear();

            // 프로젝션 : SELECT 절에 조회할 대상을 지정하는 것
            // 엔티티 프로젝션
            List<Member> result = em.createQuery("select m from Member m", Member.class)
                    .getResultList();

            Member findMember = result.get(0);
            findMember.setAge(20); // 영속성 컨텍스트에 다 반영이 된다.

            // 엔티티 프로젝션
//            List<Team> result2 = em.createQuery("select m.team from Member m", Team.class)
//                    .getResultList();  // join 쿼리가 나간다. 하지만 join쿼리를 한 눈에 보이게 작성해주는 것이 좋다.
            // 엔티티 프로젝션
            List<Team> result2 = em.createQuery("select t from Member m join m.team t", Team.class)
                    .getResultList();  // join 쿼리가 한 눈에 보인다.

            // 임베디드 타입 프로젝션
            List<Address> resultList = em.createQuery("select o.address from Order o", Address.class)
                    .getResultList();  // 어디에 소속되어있는지까지 명시해서 조회해야한다. (여기서는 Order) 이것이 값 타입의 한계 중 하나(타입만으로는 안되고 엔티티로부터 조회를 시작해야한다는 것)

            // 스칼라 타입 프로젝션
            // Query 타입으로 조회
            List resultList2 = em.createQuery("select distinct m.username, m.age from Member m")
                    .getResultList();
            // 타입을 명기를 못하니 object로 반환되어진다.
            // Object 배열로 반환되어지는데 그 배열 요소 하나하나 안에 또 Object배열이 있다.
            // 최상단에서 봤을때 보이는 배열들은 각각의 row를 의미하고, 그 안에 배열에는 한 row의 컬럼들이 저장되어 있는 것이다.
            Object o = resultList2.get(0); // 하나의 row를 추출했으면 Object[] 형태로 반환되어지는데, 그것을 Object로 받고 있다.
            Object[] o1 = (Object[]) o; // 형변환을 해주어서 사용가능한 형태로 만들어준다.
            System.out.println("username = " + o1[0]);
            System.out.println("age = " + o1[1]);
            
            //Object[] 타입으로 조회
            List<Object[]> resultList3 = em.createQuery("select distinct m.username, m.age from Member m")
                    .getResultList();  // 리스트에서 Generic 으로 Object[]을 받음으로서 형변환을 생략함
            Object[] o2 = resultList3.get(0);
            System.out.println("username = " + o2[0]);
            System.out.println("age = " + o2[1]);
            
            // new 명령어로 조회
            List<MemberDto> result3 = em.createQuery("select new jpql.MemberDto(m.username, m.age) from Member m", MemberDto.class)
                    .getResultList();
            MemberDto memberDto = result3.get(0);
            System.out.println("memberDto = " + memberDto.getUsername());
            System.out.println("memberDto = " + memberDto.getAge());

            tx.commit();
*/

/*
            // TODO : 페이징
            for(int i = 0; i < 100; i++) {
                Member member = new Member();
                member.setUsername("member" + i);
                member.setAge(i);
                em.persist(member);
            }

            em.flush();
            em.clear();

            List<Member> result4 = em.createQuery("select m from Member m order by m.age desc ", Member.class)
                    .setFirstResult(0)
//                    .setFirstResult(1)
                    .setMaxResults(10)
                    .getResultList();
            System.out.println("result4.size() = " + result4.size());
            for (Member member1 : result4) {
//                System.out.println("member1.getUsername() = " + member1.getUsername());
//                System.out.println("member1.getAge() = " + member1.getAge());
                // toString 활용
                System.out.println("member1 = " + member1);
            }

            tx.commit();
*/

/*
            // TODO : join
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
            em.persist(member);

            member.changeTeam(team);

            em.flush();
            em.clear();

//            String query = "select m from Member m join m.team t";  // inner join
//            String query = "select m from Member m left join m.team t";  // outer join
//            String query = "select m from Member m, Team t where m.username = t.name";  // theta join
//            String query = "select m from Member m left join m.team t on t.name = 'teamA'";
            String query = "select m from Member m left join Team t on m.username = t.name";  // 연관관계 없는 엔티티 외부 조인
            List<Member> result4 = em.createQuery(query, Member.class)
                    .getResultList();

            tx.commit();
*/

/*
            // TODO : JPQL 타입 표현과 기타식
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
            member.setType(MemberType.ADMIN);
            em.persist(member);

            member.changeTeam(team);

            em.flush();
            em.clear();

//            String query = "select m.username, 'HELLO', true From Member m " +
//                    "where m.type = jpql.MemberType.ADMIN";
//            List<Object[]> resultList = em.createQuery(query).getResultList();
            String query = "select m.username, 'HELLO', true From Member m " +
                    "where m.type = :userType";
            List<Object[]> resultList = em.createQuery(query)
                    .setParameter("userType", MemberType.ADMIN)
                    .getResultList();

            for (Object[] objects : resultList) {
                System.out.println("objects[0] = " + objects[0]);
                System.out.println("objects[0] = " + objects[1]);
                System.out.println("objects[0] = " + objects[2]);
            }

            tx.commit();
*/

/*
            //TODO : 조건식(CASE 등등)
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
//            member.setUsername("member1");
            member.setUsername("관리자");
            member.setType(MemberType.ADMIN);
            em.persist(member);

            member.changeTeam(team);

            em.flush();
            em.clear();

//            String query = "select " +
//                                    "case when m.age <=10 then '학생요금' " +
//                                    "     when m.age >=60 then '경로요금' " +
//                                    "     else '경로요금' " +
//                                    "end " +
//                            "from Member m ";
            // COALESCE : 하나씩 조회해서 null이 아니면 반환
//            String query = "select coalesce(m.username, '이름 없는 회원') from Member m";  // 사용자 이름이 없으면 이름 없는 회원을 반환
            // NULLIF : 두 값이 같으면 null 반환, 다르면 첫번째 값 반환
            String query = "select NULLIF(m.username, '관리자') from Member m";  // 사용자 이름이 `관리자`면 null을 반환하고, 나머지는 본인의 이름을 반환
            List<String> resultList2 = em.createQuery(query, String.class)
                    .getResultList();
            for (String s : resultList2) {
                System.out.println("s = " + s);
            }

            tx.commit();
*/

/*
            // TODO : JPQL 함수
            Member member = new Member();
            member.setUsername("관리자1");
            em.persist(member);

            Member member2 = new Member();
            member2.setUsername("관리자2");
            em.persist(member2);

            em.flush();
            em.clear();

//            String query = "select 'a' || 'b' from Member m";
//            String query = "select concat('a', 'b') from Member m";
//            String query = "select substring(m.username, 2, 3) from Member m";
//            String query = "select locate('de','abcdefg') from Member m"; // 제네릭과 class 타입 등을 Integer로 변경하여야 정상작동한다.

            String query = "";

            List<String> resultList = em.createQuery(query, String.class)
                    .getResultList();
            for (String s : resultList) {
                System.out.println("s = " + s);
            }

            tx.commit();
*/

/*
            // TODO : 경로표현식
            Team team = new Team();
            em.persist(team);

            Member member = new Member();
            member.setUsername("관리자1");
            member.setTeam(team);
            em.persist(member);

            Member member2 = new Member();
            member2.setUsername("관리자2");
            member2.setTeam(team);
            em.persist(member2);

            em.flush();
            em.clear();

//            // 단일 값 연관경로 : 묵시적 내부 조인(inner join) 발생, 탐색O
//            // 묵시적인 내부조인은 쿼리 최적화에 좋지 않다. 웬만하면 이렇게 짜면 안된다.
//            // jpql과 sql 형태를 비슷하게 가져가야 파악이 쉽다.
            String query1 = "select m.team From Member m";
            List<Team> resultList1 = em.createQuery(query1, Team.class)
                    .getResultList();
            for (Team s : resultList1) {
                System.out.println("s = " + s);
            }

            // 컬렉션값 연관경로 : 묵시적 내부조인 발생, 탐색X (FROM 절에서 명시적 조인을 통해 별칭을 얻으면 별칭을 통해 탐색 가능)
            String query2 = "select t.members From Team t";
            List<Collection> result = em.createQuery(query2, Collection.class)
                    .getResultList();
            for (Object o : result) {
                System.out.println("o = " + o);
            }
            String query3 = "select t.members.size FROM Team t";
            Integer singleResult = em.createQuery(query3, Integer.class)
                    .getSingleResult();
            System.out.println("singleResult = " + singleResult);

            // 실무에서는 묵시적 조인을 사용하지 마라!

            tx.commit();
*/

            // TODO : 페치조인1 - 기본
            Team teamA = new Team();
            teamA.setName("TeamA");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("TeamB");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();

//            // N+1문제 발생
//            String query = "select m from Member m";
//            List<Member> result = em.createQuery(query, Member.class)
//                    .getResultList();

            // 엔티티 페치 조인
            // 한번에 긁어오기 때문에 team도 프록시 객체가 아니다.
            // 지연로딩으로 설정을 해놓았어도 항상 `join fetch`가 우선으로 적용된다
            String query = "select m from Member m join fetch m.team"; 
            List<Member> result = em.createQuery(query, Member.class)
                    .getResultList();

            System.out.println("엔티티 페치 조인");
            for (Member member : result) {
                System.out.println("member = " + member.getUsername() + ", " + member.getTeam().getName());
                //회원1, 팀A(SQL)
                //회원3, 팀A(1차캐시)
                //회원3, 팀B(SQL)

                // 회원100명(다 다른팀) -> N + 1 문제 발생 
                // ( 1은 처음에 조회하려고 날린 쿼리이고, N은 부가적으로 파생되는 N개의 쿼리 -> 1+N이라고 해야 시간에 흐름순으로 보면 더 맞는 표현일수도)
            }

            // 컬렉션 페치 조인
            String query2 = "select t from Team t join fetch t.members";
            List<Team> result2 = em.createQuery(query2, Team.class)
                    .getResultList();
            System.out.println("컬렉션 페치 조인");
            for (Team team : result2) {
                // db입장에서 일대다를 조인하게 되면 데이터가 뻥튀기가 된다.
                System.out.println("team = " + team.getName() + " | members = " + team.getMembers().size() );
            }

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
           em.close();
        }

        emf.close();

    }

}