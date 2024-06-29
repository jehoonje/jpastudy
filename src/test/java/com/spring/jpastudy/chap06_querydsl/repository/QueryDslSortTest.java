package com.spring.jpastudy.chap06_querydsl.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.jpastudy.chap06_querydsl.entity.Group;
import com.spring.jpastudy.chap06_querydsl.entity.Idol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.spring.jpastudy.chap06_querydsl.entity.QIdol.idol;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
//@Rollback(false)
class QueryDslSortTest {

    @Autowired
    IdolRepository idolRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    JPAQueryFactory factory;


    @BeforeEach
    void setUp() {

        //given
        Group leSserafim = new Group("르세라핌");
        Group ive = new Group("아이브");

        groupRepository.save(leSserafim);
        groupRepository.save(ive);

        Idol idol1 = new Idol("김채원", 24, leSserafim);
        Idol idol2 = new Idol("사쿠라", 26, leSserafim);
        Idol idol3 = new Idol("가을", 22, ive);
        Idol idol4 = new Idol("리즈", 20, ive);
        Idol idol5 = new Idol("장원영", 20, ive);

        idolRepository.save(idol1);
        idolRepository.save(idol2);
        idolRepository.save(idol3);
        idolRepository.save(idol4);
        idolRepository.save(idol5);

    }


    @Test
    @DisplayName("QueryDSL로 기본 정렬하기")
    void sortingTest() {
        //given

        //when
        List<Idol> sortedIdols = factory
                .selectFrom(idol)
                .orderBy(idol.age.desc())
                .fetch();

        //then
        assertFalse(sortedIdols.isEmpty());

        System.out.println("\n\n\n");
        sortedIdols.forEach(System.out::println);
        System.out.println("\n\n\n");

        // 추가 검증 예시: 첫 번째 아이돌이 나이가 가장 많고 이름이 올바르게 정렬되었는지 확인
        assertEquals("사쿠라", sortedIdols.get(0).getIdolName());
        assertEquals(26, sortedIdols.get(0).getAge());

    }


    @Test
    @DisplayName("페이징 처리 하기")
    void pagingTest() {
        //given
        int pageNo = 1;
        int amount = 2;

        //when
        List<Idol> pagedIdols = factory
                .selectFrom(idol)
                .orderBy(idol.age.desc())
                .offset((pageNo - 1) * amount)
                .limit(amount)
                .fetch();

        // 총 데이터 수
        Long totalCount = Optional.ofNullable(factory
                .select(idol.count())
                .from(idol)
                .fetchOne()).orElse(0L);

        //then
        System.out.println("\n\n\n");
        pagedIdols.forEach(System.out::println);
        System.out.println("\n\n\n");

        System.out.println("\ntotalCount = " + totalCount);
        assertTrue(totalCount == 5);
    }



    @Test
    @DisplayName("Spring의 Page인터페이스를 통한 페이징 처리")
    void pagingWithJpaTest() {
        //given
        Pageable pageInfo = PageRequest.of(0, 2);

        //when
        Page<Idol> pagedIdols = idolRepository.foundAllByPaging(pageInfo);

        //then
        assertNotNull(pagedIdols);
        assertEquals(2, pagedIdols.getSize());

        System.out.println("\n\n\n");
        pagedIdols.getContent().forEach(System.out::println);
        System.out.println("\n\n\n");
    }


    @Test
    @DisplayName("이름 오름차순 정렬 조회")
    void testSortByNameAsc() {
        // given

        // when
        List<Idol> sortedIdols = factory
                .selectFrom(idol)
                .orderBy(idol.idolName.asc())
                .fetch();

        // then
        assertFalse(sortedIdols.isEmpty());

        System.out.println("\n\n\n");
        sortedIdols.forEach(System.out::println);
        System.out.println("\n\n\n");

        // 추가 검증 예시: 첫 번째 아이돌이 이름순으로 올바르게 정렬되었는지 확인
        assertEquals("가을", sortedIdols.get(0).getIdolName());
    }

    @Test
    @DisplayName("나이 내림차순 정렬 및 페이징 처리 조회")
    void testSortByAgeDescAndPaging() {
        // given
        int pageNumber = 0; // 첫 번째 페이지
        int pageSize = 3; // 페이지당 데이터 수

        // when
        List<Idol> pagedIdols = factory
                .selectFrom(idol)
                .orderBy(idol.age.desc())
                .offset(pageNumber * pageSize)
                .limit(pageSize)
                .fetch();

        // then
        assertNotNull(pagedIdols);
        assertEquals(pageSize, pagedIdols.size());

        System.out.println("\n\n\n");
        pagedIdols.forEach(System.out::println);
        System.out.println("\n\n\n");

        // 추가 검증 예시: 첫 번째 페이지의 첫 번째 아이돌이 나이가 가장 많은지 확인
        assertEquals("사쿠라", pagedIdols.get(0).getIdolName());
        assertEquals(26, pagedIdols.get(0).getAge());
    }

    @Test
    @DisplayName("특정 그룹의 아이돌을 이름 기준으로 오름차순 정렬 및 페이징 처리 조회")
    void testSortByNameAscAndPagingForGroup() {
        // given
        String groupName = "아이브";
        int pageNumber = 0; // 첫 번째 페이지
        int pageSize = 2; // 페이지당 데이터 수

        // when
        List<Idol> pagedIdols = factory
                .selectFrom(idol)
                .where(idol.group.groupName.eq(groupName))
                .orderBy(idol.idolName.asc())
                .offset(pageNumber * pageSize)
                .limit(pageSize)
                .fetch();

        // then
        assertNotNull(pagedIdols);
        assertEquals(pageSize, pagedIdols.size());

        System.out.println("\n\n\n");
        pagedIdols.forEach(System.out::println);
        System.out.println("\n\n\n");

        // 추가 검증 예시: 첫 번째 페이지의 첫 번째 아이돌이 이름순으로 올바르게 정렬되었는지 확인
        assertEquals("가을", pagedIdols.get(0).getIdolName());
    }




}