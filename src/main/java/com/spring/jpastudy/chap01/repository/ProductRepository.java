package com.spring.jpastudy.chap01.repository;


import com.spring.jpastudy.chap01.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository를 상속한 후 첫번째제너릭엔 엔터티클래스 타입,
// 두번째 제너릭엔 PK의 타입
public interface ProductRepository extends JpaRepository<Product, Long> {

}