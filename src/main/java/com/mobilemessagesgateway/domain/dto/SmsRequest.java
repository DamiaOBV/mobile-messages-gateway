package com.mobilemessagesgateway.domain.dto;

import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SmsRequest {

    @NotBlank(message = "Field number is required")
    private String number;
    private String text;
}
