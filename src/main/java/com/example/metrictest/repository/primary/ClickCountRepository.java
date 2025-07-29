package com.example.metrictest.repository.primary;

import com.example.metrictest.entity.primary.ClickCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Repository
public interface ClickCountRepository extends JpaRepository<ClickCount, Long> {
    @Transactional
    ClickCount findTop1ByOrderByIdDesc();

    @Transactional
    ClickCount findByTime(LocalDateTime time);
}
