package com.mobilemessagesgateway.service.sender;

import com.mobilemessagesgateway.domain.dto.provider.RESTRequest;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@CommonsLog
public class RESTService implements SenderService {

    @Autowired
    public RESTService() {
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
        RESTRequest restRequest = buildRESTRequest(text, number, url);
    }

    /**
     * buildRESTRequest
     *
     * @param text   Sms body
     * @param number phone number
     * @param url    sms destination url
     * @return RESTRequest REST provider http body
     */
    //TODO Method only for mock sendSms service.
    private RESTRequest buildRESTRequest(String text, String number, String url) {

        return RESTRequest.builder().text(text).number(number).url(url).build();
    }

}
