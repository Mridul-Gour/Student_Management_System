package com.cts.sms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LandingController {

    @GetMapping("/")
    public String showLandingPage() {
        return "landing"; // matches landing.html in templates folder
    }
}
