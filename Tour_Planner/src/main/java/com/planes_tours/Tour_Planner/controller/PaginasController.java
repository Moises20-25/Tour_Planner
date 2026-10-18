package com.planes_tours.Tour_Planner.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaginasController {

    @GetMapping("/")
    public String inicio() {
        return "index";
    }

    @GetMapping("/asesores")
    public String asesores() {
        return "asesores";
    }
}