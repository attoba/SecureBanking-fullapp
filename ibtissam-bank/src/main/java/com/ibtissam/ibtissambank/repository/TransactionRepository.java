package com.ibtissam.ibtissambank.repository;

import com.ibtissam.ibtissambank.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
}
