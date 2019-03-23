package com.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UserController {

    @RequestMapping(value = "/user/me", produces = "application/json; charset=utf-8")
    public Principal user(Principal principal) {
        return principal;
    }
}
