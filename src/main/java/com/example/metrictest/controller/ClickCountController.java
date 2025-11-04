package com.example.metrictest.controller;

import com.example.metrictest.entity.primary.ClickCount;
import com.example.metrictest.repository.primary.ClickCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ClickCountController {

    private final ClickCountRepository repository;

    @GetMapping("/")
    public String main() {
        return "index";
    }

    @PostMapping("/add")
    @org.springframework.transaction.annotation.Transactional
    public String click() {
        System.out.println("=== /add endpoint called ===");
        try {
            LocalDateTime now = LocalDateTime.now();
            // 현재 시간을 5분 단위로 그룹화 (초, 나노초 제거)
            LocalDateTime truncatedToMinute = now.truncatedTo(ChronoUnit.MINUTES);
            int minute = truncatedToMinute.getMinute();
            int groupedMinute = (minute / 5) * 5; // 0, 5, 10, 15, ... 55
            LocalDateTime fiveMinuteSlot = truncatedToMinute.withMinute(groupedMinute);
            
            System.out.println("Current time: " + now);
            System.out.println("5-minute slot: " + fiveMinuteSlot);

            // 비관적 락을 사용하여 5분 단위 시간에 해당하는 레코드 조회
            java.util.Optional<ClickCount> existingRecord = repository.findByTime(fiveMinuteSlot);
            
            if (!existingRecord.isPresent()) {
                // 해당 5분 단위에 대한 레코드가 없으면 새로 생성
                System.out.println("Creating new record for 5-minute slot: " + fiveMinuteSlot);
                try {
                    ClickCount newRecord = ClickCount.builder()
                            .time(fiveMinuteSlot)
                            .count(1)
                            .build();
                    @SuppressWarnings("null")
                    ClickCount saved = repository.save(newRecord);
                    System.out.println("New record created and saved: " + saved);
                } catch (org.springframework.dao.DataIntegrityViolationException e) {
                    // 동시 삽입으로 인한 UNIQUE 제약조건 위반 - 레코드가 이미 존재함
                    System.out.println("Record already exists due to concurrent insert, incrementing count");
                    // 다시 조회해서 카운트 증가
                    ClickCount record = repository.findByTime(fiveMinuteSlot).orElseThrow();
                    record.incrementCount();
                    repository.save(record);
                }
            } else {
                // 해당 5분 단위에 대한 레코드가 있으면 count만 증가
                ClickCount record = existingRecord.get();
                System.out.println("Incrementing count for existing record. Current count: " + record.getCount());
                record.incrementCount();
                repository.save(record);
                System.out.println("Count incremented and saved. New count: " + record.getCount());
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
    public ModelAndView list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            ModelAndView mav) {
        // 페이징 처리로 성능 개선
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<ClickCount> metricPage = repository.findAll(pageable);
        
        mav.addObject("metricPage", metricPage);
        mav.addObject("metricList", metricPage.getContent());
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
