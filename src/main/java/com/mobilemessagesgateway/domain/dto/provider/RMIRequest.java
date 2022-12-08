package com.mobilemessagesgateway.domain.dto.provider;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
//TODO Example class with some useful fields --> This should be revised for real calls
public class RMIRequest {

    private String text;
    private int prefix;
    private String number;
    private String url;
}
