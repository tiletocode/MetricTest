package com.example.metrictest.controller;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    // NullPointerException 처리
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleNullPointerException(NullPointerException ex, Model model) {
        model.addAttribute("error", "NullPointerException: " + ex.getMessage());
        return "error";  // error.html로 이동
    }

    // IllegalArgumentException 처리
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(IllegalArgumentException ex, Model model) {
        model.addAttribute("error", "IllegalArgumentException: " + ex.getMessage());
        return "error";  // error.html로 이동
    }

    // ArithmeticException 처리
    @ExceptionHandler(ArithmeticException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleArithmeticException(ArithmeticException ex, Model model) {
        model.addAttribute("error", "ArithmeticException: " + ex.getMessage());
        return "error";  // error.html로 이동
    }

    // IndexOutOfBoundsException 처리
    @ExceptionHandler(IndexOutOfBoundsException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleIndexOutOfBoundsException(IndexOutOfBoundsException ex, Model model) {
        model.addAttribute("error", "IndexOutOfBoundsException: " + ex.getMessage());
        return "error";  // error.html로 이동
    }

    // FileNotFoundException 처리
    @ExceptionHandler(FileNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleFileNotFoundException(FileNotFoundException ex, Model model) {
        model.addAttribute("error", "FileNotFoundException: " + ex.getMessage());
        return "error";  // error.html로 이동
    }

    // IOException 처리
    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleIOException(IOException ex, Model model) {
        model.addAttribute("error", "IOException: " + ex.getMessage());
        return "error";  // error.html로 이동
    }

    // ResponseStatusException 처리
    @ExceptionHandler(ResponseStatusException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResponseStatusException(ResponseStatusException ex, Model model) {
        model.addAttribute("error", "ResponseStatusException: " + ex.getMessage());
        return "error";  // error.html로 이동
    }
}
