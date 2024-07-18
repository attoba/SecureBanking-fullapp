package com.ibtissam.ibtissambank.dto;


import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequest {
    //private String sourceAccountNumber;
    private String destinationAccountNumber;
    private BigDecimal amount;
}