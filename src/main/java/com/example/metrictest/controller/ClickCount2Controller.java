package com.example.metrictest.controller;

import com.example.metrictest.entity.secondary.ClickCount2;
import com.example.metrictest.repository.secondary.ClickCount2Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Controller
@RequestMapping("/db2")
@RequiredArgsConstructor
public class ClickCount2Controller {

    private final ClickCount2Repository repository;

    @GetMapping("/")
    public String main() {
        return "index-db2";
    }

    @PostMapping("/count")
    @SuppressWarnings("null")
    public String count() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        ClickCount2 existingRecord = repository.findByTime(now);

        if (existingRecord != null) {
            existingRecord.incrementCount();
            repository.save(existingRecord);
        } else {
            ClickCount2 newRecord = ClickCount2.builder()
                    .time(now)
                    .count(1)
                    .build();
            repository.save(newRecord);
        }

        return "redirect:/db2/";
    }

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("clickCounts", repository.findAll(Sort.by(Sort.Direction.DESC, "time")));
        return "list";
    }
}
