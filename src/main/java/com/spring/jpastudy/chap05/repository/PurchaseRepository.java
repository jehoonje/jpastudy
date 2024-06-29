package com.spring.jpastudy.chap05.repository;

import com.spring.jpastudy.chap05.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {


}
