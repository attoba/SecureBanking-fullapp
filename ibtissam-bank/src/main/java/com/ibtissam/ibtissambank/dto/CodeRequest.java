package com.ibtissam.ibtissambank.dto;

import lombok.*;


@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodeRequest {
    private String inputCode;
    private String email;

}
