package com.example.metrictest.entity.primary;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class ClickCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    // @DateTimeFormat(pattern="yy-MM-dd HH:mm")
    private LocalDateTime time;

    @Column
    private int count;

    @Builder
    public ClickCount(Long id, LocalDateTime time, int count) {
        this.id = id;
        this.time = time;
        this.count = count;
    }

    // count를 1 증가시키는 메서드
    public void incrementCount() {
        this.count++;
    }
}
