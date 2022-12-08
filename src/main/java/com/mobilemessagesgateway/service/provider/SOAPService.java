package com.mobilemessagesgateway.service.provider;

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
     * @param prefix number prefix
     * @param number phone number
     * @param url    sms destination url
     */
    //TODO Mock Service - Real SMS sending not implemented yet
    public void sendSms(String text, int prefix, String number, String url) {
        log.info("Sending SMS using SOAP protocol");
        SOAPRequest soapRequest = buildSOAPRequest(text, prefix, number, url);
        log.info(soapRequest);
    }

    /**
     * buildSOAPRequest
     *
     * @param text   Sms body
     * @param prefix number prefix
     * @param number phone number
     * @param url    sms destination url
     * @return SOAPRequest SOAP provider http body
     */
    //TODO Method only for mock sendSms service.
    private SOAPRequest buildSOAPRequest(String text, int prefix, String number, String url) {

        return SOAPRequest.builder().text(text).prefix(prefix).number(number).url(url).build();
    }

}
