package com.ibtissam.ibtissambank.repository;


import com.ibtissam.ibtissambank.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);
    @Query("SELECT u FROM User u WHERE u.email = :email")
    User findByEmail(@Param("email") String email);
    Boolean existsByAccountNumber(String accountNumber);

    @Query("SELECT u FROM User u WHERE u.accountNumber = :accountNumber")
    User findByAccountNumber(@Param("accountNumber") String accountNumber);

}
