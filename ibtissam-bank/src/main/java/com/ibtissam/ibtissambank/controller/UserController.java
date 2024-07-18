package com.ibtissam.ibtissambank.controller;


import com.ibtissam.ibtissambank.dto.*;
import com.ibtissam.ibtissambank.service.UserService;
import com.ibtissam.ibtissambank.service.UserServiceImp;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/client")
public class UserController {

    Logger logger= LogManager.getLogger(UserServiceImp.class);
    @Autowired
    UserService userService;

    @Secured("ROLE_USER")
    @PostMapping
    public BankResponse createAccount(@RequestBody UserRequest userRequest){
        logger.info("account created with email: {}", userRequest.getEmail());

        return userService.createAccount(userRequest);
    }

    @PostMapping("/logout")
    public BankResponse logout(@RequestHeader("Authorization") String tokenHeader){
        return userService.logout(tokenHeader);
    }

    @Secured("ROLE_USER")
    @PostMapping("/login")
    public BankResponse login(@RequestBody LoginDto loginDto){
        logger.info("Login attempt for username: {}", loginDto.getEmail());
        return userService.login(loginDto);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/loginE")
    public BankResponse loginEmploye(@RequestBody LoginDto loginDto){
        logger.info("Login attempt for as employe with username: {}", loginDto.getEmail());
        return userService.loginEmploye(loginDto);
    }

    @Secured("ROLE_USER")
    @PostMapping("/verify")
    public BankResponse verify(@RequestBody CodeRequest codeRequest) {


        logger.info("verifying login with Received enteredCode: " + codeRequest.getInputCode());
        return userService.verify(codeRequest);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/debit")
    public BankResponse debitAccount(@RequestBody CreditDebitRequest request){
        logger.info("Debit request for account: {}", request.getAccountNumber());
        logger.info("Debit request for account to amount : {}", request.getAmount());
        return userService.debitAccount(request);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/credit")
    public BankResponse creditAccount(@RequestBody CreditDebitRequest request) {
        logger.info("Credit request for account: {}", request.getAccountNumber(),request.getAmount());
        return userService.creditAccount(request);
    }

    @Secured("ROLE_USER")
    @PostMapping("/transfer")
    public BankResponse transfer(@RequestBody TransferRequest request) {
        logger.info("Transfer request to account {}",
                request.getDestinationAccountNumber(),request.getAmount());
        return userService.transfer(request);
    }
    @Secured("ROLE_USER")
    @GetMapping("/balance")
    public BankResponse getBalance(Authentication authentication) {

        return userService.getBalance(authentication);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/deleteUser")
    public BankResponse deleteUser(@RequestParam String accountNumber) {
        logger.info("User with account number {} deleted ", accountNumber);

        return userService.deleteUser(accountNumber);
    }
}
