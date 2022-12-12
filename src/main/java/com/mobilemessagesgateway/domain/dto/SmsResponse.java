package com.mobilemessagesgateway.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SmsResponse {

    private long id;
    private String provider;
    private String status;
}
