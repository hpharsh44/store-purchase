package com.store.store.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getHome() {
        return "Welcome Store Management Backend Application.";
    }
}
