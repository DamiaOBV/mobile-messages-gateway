package com.mobilemessagesgateway.domain.dto.provider;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@XmlRootElement
//TODO Example class with some useful fields --> This should be revised for real calls
public class SOAPRequest {
    @XmlElement
    private String text;
    @XmlElement
    private int prefix;
    @XmlElement
    private String number;
    @XmlElement
    private String url;
}
