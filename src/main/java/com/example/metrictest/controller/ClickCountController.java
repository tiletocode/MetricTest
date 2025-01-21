package com.example.metrictest;

import com.example.metrictest.entity.ClickCount;
import com.example.metrictest.repository.ClickCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

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
        Long id = repository.findTop1ByOrderByIdDesc().getId();
        LocalDateTime time = repository.findTop1ByOrderByIdDesc().getTime();
        int count = repository.findTop1ByOrderByIdDesc().getCount();

        ClickCount c = ClickCount.builder()
                .id(id)
                .time(time)
                .count(count+1).build();

        repository.save(c);
        return "redirect:/";
    }

    @GetMapping("/list")
    public ModelAndView list(ModelAndView mav) {
        Iterable<ClickCount> metricList = repository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        mav.addObject("metricList", metricList);
        mav.setViewName("list");
        return mav;
    }
}
