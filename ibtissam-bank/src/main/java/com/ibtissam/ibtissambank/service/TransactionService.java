package com.ibtissam.ibtissambank.service;

import com.ibtissam.ibtissambank.dto.TransactionDto;
import com.ibtissam.ibtissambank.entity.Transaction;

public interface TransactionService {
    void saveTransaction(TransactionDto transactionDto);
}
