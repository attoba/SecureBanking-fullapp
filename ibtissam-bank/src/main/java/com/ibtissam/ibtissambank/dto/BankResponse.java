package com.ibtissam.ibtissambank.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankResponse {
    private String responseCode;
    private String responseMessage;
    private AccountInfo accountInfo;

    public BankResponse(String responseCode, String responseMessage, Object o) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }
}