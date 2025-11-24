package com.skistation.reservationms.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/reservations")
public class ReservationController {

    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
