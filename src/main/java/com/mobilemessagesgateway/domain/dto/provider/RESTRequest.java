package com.mobilemessagesgateway.domain.dto.provider;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
//TODO Example class with some useful fields --> This should be revised for real calls
public class RESTRequest {

    private String text;
    private int prefix;
    private String number;
    private String url;
}
