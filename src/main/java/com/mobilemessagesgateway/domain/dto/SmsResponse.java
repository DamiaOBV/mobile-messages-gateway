package com.mobilemessagesgateway.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SmsResponse {

    private long id;
    private String provider;
    private String status;
}
