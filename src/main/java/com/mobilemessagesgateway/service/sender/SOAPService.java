package com.mobilemessagesgateway.service.sender;

import com.mobilemessagesgateway.domain.dto.provider.SOAPRequest;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@CommonsLog
public class SOAPService implements SenderService {

    @Autowired
    public SOAPService() {
    }

    /**
     * sendSms
     *
     * @param text   Sms body
     * @param number phone number
     * @param url    sms destination url
     */
    //TODO Mock Service - Real SMS sending not implemented yet
    public void sendSms(String text, String number, String url) {
        log.info("Sending sms: '" + text + "' to '" + number + "' using REST protocol. Provider url: '" + url + "'");
        SOAPRequest soapRequest = buildSOAPRequest(text, number, url);
    }

    /**
     * buildSOAPRequest
     *
     * @param text   Sms body
     * @param number phone number
     * @param url    sms destination url
     * @return SOAPRequest SOAP provider http body
     */
    //TODO Method only for mock sendSms service.
    private SOAPRequest buildSOAPRequest(String text, String number, String url) {

        return SOAPRequest.builder().text(text).number(number).url(url).build();
    }

}
