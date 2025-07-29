package com.example.metrictest.repository.secondary;

import com.example.metrictest.entity.secondary.ClickCount2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Repository
public interface ClickCount2Repository extends JpaRepository<ClickCount2, Long> {
    @Transactional
    ClickCount2 findTop1ByOrderByIdDesc();

    @Transactional
    ClickCount2 findByTime(LocalDateTime time);
}
