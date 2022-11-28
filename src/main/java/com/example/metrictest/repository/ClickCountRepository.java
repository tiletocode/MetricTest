package com.example.metrictest.repository;

import com.example.metrictest.entity.ClickCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ClickCountRepository extends JpaRepository<ClickCount, Long> {
    @Transactional
    ClickCount findTop1ByOrderByIdDesc();
}
