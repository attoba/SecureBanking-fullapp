package com.ibtissam.ibtissambank.service;

import com.ibtissam.ibtissambank.dto.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface UserService {
    BankResponse createAccount(UserRequest userRequest);
    BankResponse login(LoginDto loginDto);

    BankResponse loginEmploye(LoginDto loginDto);
    BankResponse verify(CodeRequest codeRequest);
    BankResponse deleteUser(String accountNumber);

    BankResponse getBalance(Authentication authentication);

    BankResponse creditAccount(CreditDebitRequest request);
    BankResponse debitAccount(CreditDebitRequest request);
    BankResponse transfer(TransferRequest request);

    BankResponse logout(String tokenHeader);


}
