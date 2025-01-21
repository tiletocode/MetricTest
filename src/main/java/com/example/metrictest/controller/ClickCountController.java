package com.example.metrictest.controller;

import com.example.metrictest.entity.ClickCount;
import com.example.metrictest.repository.ClickCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
                .count(count + 1).build();

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

    @GetMapping("/404")
    public String ex() throws IOException {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "status: 404");
    }

    @GetMapping("/ioexception")
    public String triggerIOException() throws IOException {
        throw new IOException("Manually triggered IOException");
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)  // 500 에러 상태 코드 설정
    public String handleNullPointerException(NullPointerException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error"; // error.html로 이동
    }

    @GetMapping("/illegalargument")
    public String triggerIllegalArgumentException() {
        throw new IllegalArgumentException("Invalid argument provided");
    }

    @GetMapping("/arithmetic")
    public String triggerArithmeticException() {
        int result = 10 / 0; // 여기서 ArithmeticException 발생
        return "index";
    }

    @GetMapping("/indexoutofbounds")
    public String triggerIndexOutOfBoundsException() {
        int[] arr = new int[2];
        int value = arr[5]; // 여기서 IndexOutOfBoundsException 발생
        return "index";
    }

    @GetMapping("/filenotfound")
    public String triggerFileNotFoundException() throws FileNotFoundException {
        File file = new File("nonexistentfile.txt");
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: nonexistentfile.txt");
        }
        return "index";
    }


    /*
     * org.springframework.web.util.NestedServletException,
     * org.apache.jasper.JasperException,
     * org.springframework.web.HttpRequestMethodNotSupportedException,
     * java.lang.NullPointerException,
     * java.io.IOException
     */

}
