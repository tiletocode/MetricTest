package com.example.metrictest.entity.secondary;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "click_count2")
@Getter
@NoArgsConstructor
public class ClickCount2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDateTime time;

    @Column
    private int count;

    @Builder
    public ClickCount2(Long id, LocalDateTime time, int count) {
        this.id = id;
        this.time = time;
        this.count = count;
    }

    // count를 1 증가시키는 메서드
    public void incrementCount() {
        this.count++;
    }
}
