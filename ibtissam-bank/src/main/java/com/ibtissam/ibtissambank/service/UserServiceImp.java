package com.ibtissam.ibtissambank.service;

import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.encrypt.TextEncryptor;

import com.ibtissam.ibtissambank.dto.*;
import com.ibtissam.ibtissambank.entity.Role;
import com.ibtissam.ibtissambank.entity.User;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ibtissam.ibtissambank.repository.UserRepository;
import com.ibtissam.ibtissambank.security.JwtTokenProvider;
import com.ibtissam.ibtissambank.utils.AccountUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Service
public class UserServiceImp implements UserService{
    Logger logger= LogManager.getLogger(UserServiceImp.class);

    @Autowired
    TokenBlacklistService tokenBlacklistService;
    @Autowired
    CustomUserDetailsService userDetailsService ;


    //@Autowired
   // private CsrfTokenRepository csrfTokenRepository;
    @Autowired
    private VerificationCodeService verificationCodeService;

    final Map<String, VerificationInfo> verificationCodes = new ConcurrentHashMap<>();

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Autowired
    EmailService emailService;


    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    TransactionService transactionService;


    @Override
    public BankResponse createAccount(UserRequest userRequest) {

        if (userRepository.existsByEmail(userRequest.getEmail())){
            //logger.info("account created with email: {}", userRequest.getEmail());

            return  new BankResponse(AccountUtils.ACCOUNT_EXISTS_CODE,AccountUtils.ACCOUNT_EXISTS_MESSAGE,null);

        }
        String accountNumber = AccountUtils.generateAccountNumber();

        // Email verification code to the user
        EmailDetails AccountCreated = EmailDetails.builder()
                .subject("Account created")
                .recipient(userRequest.getEmail())
                .messageBody("Your number account is: " +accountNumber + " with name : "+userRequest.getFirstName() + " " + userRequest.getLastName())
                .build();
        emailService.sendEmailAlert(AccountCreated);



        String salt = KeyGenerators.string().generateKey();
        // Create TextEncryptor using the salt
        BytesEncryptor encryptor = Encryptors.stronger("AZJEcvx9f1/0hRwsGoplmN4zq,y17Ddbvkj;PQO0XZ20.", salt);

        // Encrypt the account number
        byte[] encryptedAccountNumber = encryptor.encrypt(accountNumber.getBytes());
        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .country(userRequest.getCountry())
                .accountNumber(Arrays.toString(encryptedAccountNumber))
                .accountBalance(BigDecimal.ZERO)
                .email(userRequest.getEmail())
                .cin(userRequest.getCin())
                .phoneNumber(userRequest.getPhoneNumber())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .status("ACTIVE")
                .role(Role.valueOf("ROLE_USER"))
                .build();

        User savedUser = userRepository.save(newUser);
        return BankResponse.builder()
                .responseCode("SUCCESS")
                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName() + " " + savedUser.getLastName() )
                        .build())
                .build();
    }


    public BankResponse login(LoginDto loginDto){
        try {

        Authentication authentication = null;
        authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword())
        );
        logger.info("Successful login attempt for username: {}", loginDto.getEmail());
        // Generate verification code
        String Code = verificationCodeService.generateVerificationCode();


        long expirationTime = System.currentTimeMillis() + 10 * 60 * 1000; // 5 minutes expiry


        // Map to store verification codes and their expiration times generated
        verificationCodes.put(loginDto.getEmail(), new VerificationInfo(Code, System.currentTimeMillis(), expirationTime));


        // Email verification code to the user
        EmailDetails verificationEmail = EmailDetails.builder()
                .subject("Your Verification Code")
                .recipient(loginDto.getEmail())
                .messageBody("Your verification code is: " + Code)
                .build();
        emailService.sendEmailAlert(verificationEmail);



        return BankResponse.builder()
                .responseCode("Login Success")
                .responseMessage("Login Success")
                .build();
    }
        catch (AuthenticationException e){
            logger.info("Failed login attempt for username: {}", loginDto.getEmail());
            
        }
        return null;
    }

    public BankResponse loginEmploye(LoginDto loginDto) {
        Authentication authentication = null;

        // Perform authentication
        authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );

        // Check if the user has the ROLE_ADMIN role
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            // If the user has ROLE_ADMIN, generate the token and return success response
           // logger.info("Login attempt for as employe with username: {}", loginDto.getEmail());

            return BankResponse.builder()
                    .responseCode("Login Success")
                    .responseMessage(jwtTokenProvider.generateToken(authentication))
                    .build();
        } else {
            // If the user does not have ROLE_ADMIN, return an error response
            return BankResponse.builder()
                    .responseCode("Login Failed")
                    .responseMessage("User does not have the required admin role.")
                    .build();
        }
    }


    @Override
    public BankResponse verify(CodeRequest codeRequest) {
        /*String email = codeRequest.getEmail();
        String enteredCode = codeRequest.getInputCode();

        // Check if the entered verification code is valid
        VerificationInfo verificationInfo = verificationCodes.get(email);
        if ( verificationInfo.getVerificationCode().equals(enteredCode) ) {
            // Clear verification code from map
            //verificationCodes.remove(email);
            return BankResponse.builder()
                    .responseCode("Verification Success")
                    //.responseCode(enteredCode)
                   // .responseMessage(verificationInfo.getVerificationCode())
                    .responseMessage(enteredCode)
                    .build();
        } else {
            return BankResponse.builder()
                    .responseCode("Failed")
                  //  .responseCode(verificationInfo.getVerificationCode())

                    .responseMessage(enteredCode)
                    .build();
        }*/

        String email = codeRequest.getEmail();
        String enteredCode = codeRequest.getInputCode();


        // Get the source account user from the logged-in email
        User sourceAccountUser = userRepository.findByEmail(email);

        // Authentication step to generate JWT token
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);




        // Check if the entered verification code is valid
        VerificationInfo verificationInfo = verificationCodes.get(email);
        if ( verificationInfo.getVerificationCode().equals(enteredCode) ) {
            // Clear verification code from map
            //verificationCodes.remove(email);
            //logger.info("verifying login with Received enteredCode: " + codeRequest.getInputCode());

            return BankResponse.builder()
                    .responseCode("Verification Success")
                    //.responseCode(enteredCode)
                    // .responseMessage(verificationInfo.getVerificationCode())
                    .responseMessage(jwtTokenProvider.generateToken(authentication))
                    .build();
        } else {
            return BankResponse.builder()
                    .responseCode("Verification Failed")
                    //  .responseCode(verificationInfo.getVerificationCode())

                    .responseMessage(enteredCode)
                    .build();
        }
    }


    public BankResponse creditAccount(CreditDebitRequest request) {
        //checking if the account exists
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User userToCredit = userRepository.findByAccountNumber(request.getAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));
        userRepository.save(userToCredit);

        //Save transaction
        TransactionDto transactionDto = TransactionDto.builder()
                .accountNumber(userToCredit.getAccountNumber())
                .transactionType("CREDIT")
                .amount(request.getAmount())
                .build();

        transactionService.saveTransaction(transactionDto);
       // logger.info("Credit request for account {} with amount {}", request.getAccountNumber(),request.getAmount());

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(userToCredit.getFirstName() + " " + userToCredit.getLastName())
                        .accountBalance(userToCredit.getAccountBalance())
                        .accountNumber(request.getAccountNumber())
                        .build())
                .build();

    }
    public BankResponse debitAccount(CreditDebitRequest request) {
        //check if the account exists
        //check if the amount you intend to withdraw is not more than the current account balance
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User userToDebit = userRepository.findByAccountNumber(request.getAccountNumber());
        BigInteger availableBalance =userToDebit.getAccountBalance().toBigInteger();
        BigInteger debitAmount = request.getAmount().toBigInteger();
        if ( availableBalance.intValue() < debitAmount.intValue()){
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
         else {
            userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));
            userRepository.save(userToDebit);
            TransactionDto transactionDto = TransactionDto.builder()
                    .accountNumber(userToDebit.getAccountNumber())
                    .transactionType("CREDIT")
                    .amount(request.getAmount())
                    .build();

            transactionService.saveTransaction(transactionDto);
            //logger.info("Debit request for account: {} with amount : {}", request.getAccountNumber(), request.getAmount());

            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS)
                    .responseMessage(AccountUtils.ACCOUNT_DEBITED_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountNumber(request.getAccountNumber())
                            .accountName(userToDebit.getFirstName() + " " + userToDebit.getLastName() )
                            .accountBalance(userToDebit.getAccountBalance())
                            .build())
                    .build();
        }
        }


    public BankResponse transfer(TransferRequest request) {
        //get the account to debit (check if it exists)
        //check if the amount i'm debiting is not more than the current balance
        //debit the account
        //get the account to credit
        //credit the account
/*
        // Get the logged-in user's details
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String loggedInEmail;

        if (principal instanceof LoginDto) {
            loggedInEmail = ((LoginDto) principal).getEmail();
        } else if (principal instanceof UserDetails) {
            loggedInEmail = ((UserDetails) principal).getUsername(); // Assuming username is the email
        } else {
            loggedInEmail = principal.toString();
        }

        // Logging for debugging
        logger.info("Logged-in user's email: {}", loggedInEmail);

        // Get the source account user
        User sourceAccountUser = userRepository.findByAccountNumber(request.getSourceAccountNumber());

        // Validate if the source account belongs to the logged-in user
        if (sourceAccountUser == null || !sourceAccountUser.getEmail().equals(loggedInEmail)) {
            logger.error("Unauthorized access attempt: logged in user email does not match source account email.");
            return BankResponse.builder()
                    .responseCode(AccountUtils.UNAUTHORIZED_ACCESS_CODE)
                    .responseMessage("You are not authorized to perform this transaction.")
                    .accountInfo(null)
                    .build();
        }
*/
        // Get the logged-in user's details
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String loggedInEmail;

        if (principal instanceof LoginDto) {
            loggedInEmail = ((LoginDto) principal).getEmail();
        } else if (principal instanceof UserDetails) {
            loggedInEmail = ((UserDetails) principal).getUsername(); // Assuming username is the email
        } else {
            loggedInEmail = principal.toString();
        }

        // Get the source account user from the logged-in email
        User sourceAccountUser = userRepository.findByEmail(loggedInEmail);

        // Validate if the user has the source account number
        if (sourceAccountUser == null) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.UNAUTHORIZED_ACCESS_CODE)
                    .responseMessage("You are not authorized to perform this transaction.")
                    .accountInfo(null)
                    .build();
        }

        // Proceed with the transfer logic
        //logger.info("Transfer request an amount of {} to account {}", request.getDestinationAccountNumber(),request.getAmount());
        boolean isDestinationAccountExist = userRepository.existsByAccountNumber(request.getDestinationAccountNumber());
        if (!isDestinationAccountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

       // User sourceAccountNumber = userRepository.findByAccountNumber(request.getSourceAccountNumber());
        if (request.getAmount().compareTo(sourceAccountUser.getAccountBalance()) > 0){
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance().subtract(request.getAmount()));
        String sourceUsername = sourceAccountUser.getFirstName() + " " + sourceAccountUser.getLastName() ;

        userRepository.save(sourceAccountUser);
        EmailDetails debitAlert = EmailDetails.builder()
                .subject("DEBIT ALERT")
                .recipient(sourceAccountUser.getEmail())
                .messageBody("The sum of " + request.getAmount() + " has been deducted from your account! Your current balance is " + sourceAccountUser.getAccountBalance())
                .build();

        emailService.sendEmailAlert(debitAlert);

        User destinationAccountUser = userRepository.findByAccountNumber(request.getDestinationAccountNumber());
        destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(request.getAmount()));
//        String recipientUsername = destinationAccountUser.getFirstName() + " " + destinationAccountUser.getLastName() + " " + destinationAccountUser.getOtherName();
        userRepository.save(destinationAccountUser);
        EmailDetails creditAlert = EmailDetails.builder()
                .subject("CREDIT ALERT")
                .recipient(destinationAccountUser.getEmail())
                .messageBody("The sum of " + request.getAmount() + " has been sent to your account from " + sourceUsername + " Your current balance is " + sourceAccountUser.getAccountBalance())
                .build();
        emailService.sendEmailAlert(creditAlert);

        TransactionDto transactionDto = TransactionDto.builder()
                .accountNumber(destinationAccountUser.getAccountNumber())
                .transactionType("CREDIT")
                .amount(request.getAmount())
                .build();

        transactionService.saveTransaction(transactionDto);

        return BankResponse.builder()
                .responseCode(AccountUtils.TRANSFER_SUCCESSFUL_CODE)
                .responseMessage("Transfer Successful")
                .accountInfo(AccountInfo.builder()
                        .accountBalance(sourceAccountUser.getAccountBalance())
                        .build())
                .build();

    }

    public BankResponse logout( String tokenHeader) {
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            String token = tokenHeader.substring(7);
            Date expiryDate = jwtTokenProvider.getExpiryDateFromToken(token);
            tokenBlacklistService.blacklistToken(token, expiryDate);
            return BankResponse.builder()
                    .responseCode("010")
                    .responseMessage("Logout successful")
                    .accountInfo(null)
                    .build();
        }
        return BankResponse.builder()
                .responseCode("011")
                .responseMessage("Invalid token")
                .accountInfo(null)
                .build();

    }

    public BankResponse getBalance(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);

        if (user != null) {
            return BankResponse.builder()
                    .responseCode("SUCCESS")
                    .responseMessage("Balance retrieved successfully")
                    .accountInfo(AccountInfo.builder()
                            .accountBalance(user.getAccountBalance())
                            .build())
                    .build();
        } else {
            return BankResponse.builder()
                    .responseCode("ERROR")
                    .responseMessage("User not found")
                    .accountInfo(null)
                    .build();
        }
    }


    public BankResponse deleteUser(String accountNumber) {

        User user = userRepository.findByAccountNumber(accountNumber);

        if (user != null) {
         //   logger.info("User with account number {} deleted ", accountNumber);

            userRepository.delete(user);
            return BankResponse.builder()
                    .responseCode("SUCCESS")
                    .responseMessage("User deleted successfully")
                    .build();
        } else {
            return BankResponse.builder()
                    .responseCode("ERROR")
                    .responseMessage("User not found")
                    .build();
        }
    }
}




