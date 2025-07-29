package com.example.metrictest.controller;

import com.example.metrictest.entity.primary.ClickCount;
import com.example.metrictest.repository.primary.ClickCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Controller
@RequiredArgsConstructor
public class ClickCountController {

    private final ClickCountRepository repository;

    @GetMapping("/")
    public String main() {
        return "index";
    }

    @PostMapping("/add")
    public String click() {
        System.out.println("=== /add endpoint called ===");
        try {
            LocalDateTime now = LocalDateTime.now();
            // 현재 시간을 분 단위로 truncate (초, 나노초 제거)
            LocalDateTime currentMinute = now.truncatedTo(ChronoUnit.MINUTES);
            System.out.println("Current minute: " + currentMinute);

            // 현재 분에 해당하는 레코드가 있는지 확인
            ClickCount existingRecord = repository.findByTime(currentMinute);
            System.out.println("Existing record for current minute: " + existingRecord);

            if (existingRecord == null) {
                // 현재 분에 대한 레코드가 없으면 새로 생성
                System.out.println("Creating new record for minute: " + currentMinute);
                ClickCount newRecord = ClickCount.builder()
                        .time(currentMinute)
                        .count(1)
                        .build();
                repository.save(newRecord);
                System.out.println("New record created and saved: " + newRecord);
            } else {
                // 현재 분에 대한 레코드가 있으면 count만 증가
                System.out
                        .println("Incrementing count for existing record. Current count: " + existingRecord.getCount());
                existingRecord.incrementCount(); // count를 1 증가시키는 메서드 필요
                repository.save(existingRecord);
                System.out.println("Count incremented and saved. New count: " + existingRecord.getCount());
            }

            System.out.println("Returning redirect:/");
            return "redirect:/";
        } catch (Exception e) {
            System.err.println("=== Exception occurred in /add ===");
            e.printStackTrace();
            throw new RuntimeException("Error occurred while processing click: " + e.getMessage(), e);
        }
    }

    @GetMapping("/list")
    public ModelAndView list(ModelAndView mav) {
        Iterable<ClickCount> metricList = repository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        mav.addObject("metricList", metricList);
        mav.setViewName("list");
        return mav;
    }
}
