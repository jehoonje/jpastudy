package com.spring.jpastudy.chap01.repository;

import com.spring.jpastudy.chap01.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.spring.jpastudy.chap01.entity.Product.Category.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @BeforeEach
    void insertBeforeTest() {

        Product p1 = Product.builder()
                .name("아이폰")
                .category(ELECTRONIC)
                .price(2000000)
                .build();
        Product p2 = Product.builder()
                .name("탕수육")
                .category(FOOD)
                .price(20000)
                .build();
        Product p3 = Product.builder()
                .name("구두")
                .category(FASHION)
                .price(300000)
                .build();
        Product p4 = Product.builder()
                .name("주먹밥")
                .category(FOOD)
                .price(1500)
                .build();

        productRepository.save(p1);
        productRepository.save(p2);
        productRepository.save(p3);
        productRepository.save(p4);

    }


    @Test
    @DisplayName("상품을 데이터베이스에 저장한다")
    void saveTest() {
        //given
        Product product = Product.builder()
                .name("떡볶이")
//                .price(90000)
//                .category(Product.Category.FASHION)
                .build();
        //when
        // insert후 저장된 데이터의 객체를 반환
        Product saved = productRepository.save(product);
        //then
        assertNotNull(saved);
    }


    @Test
    @DisplayName("1번 상품을 삭제한다")
    void deleteTest() {
        //given
        Long id = 1L;
        //when
        productRepository.deleteById(id);
        //then
        Product foundProduct = productRepository.findById(id)
                .orElse(null);

        assertNull(foundProduct);

    }


    @Test
    @DisplayName("3번 상품을 단일조회하면 그 상품명이 구두이다.")
    void findOneTest() {
        //given
        Long id = 3L;
        //when
        Product foundProduct = productRepository.findById(id).orElse(null);
        //then
        assertEquals("구두", foundProduct.getName());

        System.out.println("\n\n\nfoundProduct = " + foundProduct + "\n\n\n\n");
    }


    @Test
    @DisplayName("상품을 전체조회하면 상품의 총 개수가 4개이다.")
    void findAllTest() {
        //given

        //when
        List<Product> productList = productRepository.findAll();

        //then
        System.out.println("\n\n\n");

        productList.forEach(System.out::println);

        System.out.println("\n\n\n");

        assertEquals(4, productList.size());
    }




}