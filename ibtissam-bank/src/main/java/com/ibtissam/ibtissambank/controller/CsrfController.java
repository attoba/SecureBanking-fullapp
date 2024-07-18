package com.ibtissam.ibtissambank.controller;

import com.ibtissam.ibtissambank.service.UserServiceImp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CsrfController {
    Logger logger= LogManager.getLogger(UserServiceImp.class);

    @GetMapping("/csrf")
    public CsrfToken csrf(CsrfToken csrftoken){
        System.out.println("Token generated {} "+ csrftoken.getToken());

        logger.info("Token generated {} ", csrftoken.getToken());

        return csrftoken;

    }
}
