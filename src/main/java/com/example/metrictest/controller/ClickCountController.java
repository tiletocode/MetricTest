package com.example.metrictest.controller;

import com.example.metrictest.entity.primary.ClickCount;
import com.example.metrictest.repository.primary.ClickCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.Random;

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

    @GetMapping("/guid")
    @ResponseBody
    public String generateGuid() {
        System.out.println("=== /guid endpoint called ===");
        try {
            int randomSleepTime = 1000 + (int)(Math.random() * 4000); // 1000~5000ms
            System.out.println("Sleeping for " + randomSleepTime + "ms");
            Thread.sleep(randomSleepTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread interrupted during sleep: " + e.getMessage());
        }
        String guid = UUID.randomUUID().toString();
        getUserId();
        getRemoteIp();
        
        System.out.println("Generated GUID: " + guid);
        System.out.println("=== GUID response sent ===");
        return guid;
    }

 
    private String getUserId() {
            String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder userId = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int randomIndex = (int)(Math.random() * alphabet.length());
            userId.append(alphabet.charAt(randomIndex));
        }
        String result = userId.toString();
        System.out.println("Generated User Id: " + result);
        return result;
    }

    private String getRemoteIp() {
        // 임의의 IP 주소 생성 (1-255 범위)
        int octet1 = 1 + (int)(Math.random() * 254); // 1-254
        int octet2 = (int)(Math.random() * 256); // 0-255
        int octet3 = (int)(Math.random() * 256); // 0-255
        int octet4 = 1 + (int)(Math.random() * 254); // 1-254
        
        String ip = octet1 + "." + octet2 + "." + octet3 + "." + octet4;
        System.out.println("Generated Remote Ip: " + ip);
        return ip;
    }
}
