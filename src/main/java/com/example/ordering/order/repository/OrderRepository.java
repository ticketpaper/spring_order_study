package com.example.ordering.order.repository;

import com.example.ordering.order.domain.Ordering;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Ordering, Long> {
    List<Ordering> findByMemberId(Long id);
}
