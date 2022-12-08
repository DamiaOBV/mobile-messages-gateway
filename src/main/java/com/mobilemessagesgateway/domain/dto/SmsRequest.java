package com.mobilemessagesgateway.domain.dto;

import javax.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SmsRequest {

    @NotEmpty(message = "Field number is required")
    private String number;
    private String text;
}
