package com.mobilemessagesgateway.service.provider;

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
     * @param prefix number prefix
     * @param number phone number
     * @param url    sms destination url
     */
    //TODO Mock Service - Real SMS sending not implemented yet
    public void sendSms(String text, int prefix, String number, String url) {
        log.info("Sending SMS using REST protocol");
        RESTRequest restRequest = buildRESTRequest(text, prefix, number, url);
        log.info(restRequest);
    }

    /**
     * buildRESTRequest
     *
     * @param text   Sms body
     * @param prefix number prefix
     * @param number phone number
     * @param url    sms destination url
     * @return RESTRequest REST provider http body
     */
    //TODO Method only for mock sendSms service.
    private RESTRequest buildRESTRequest(String text, int prefix, String number, String url) {

        return RESTRequest.builder().text(text).prefix(prefix).number(number).url(url).build();
    }

}
