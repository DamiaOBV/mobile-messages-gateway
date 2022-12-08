package com.mobilemessagesgateway.service.provider;

import com.mobilemessagesgateway.domain.dto.provider.RMIRequest;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@CommonsLog
public class RMIService implements SenderService {

    @Autowired
    public RMIService() {
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
        log.info("Sending RMI using REST protocol");
        RMIRequest rmiRequest = buildRMIRequest(text, prefix, number, url);
        log.info(rmiRequest);
    }

    /**
     * buildRMIRequest
     *
     * @param text   Sms body
     * @param prefix number prefix
     * @param number phone number
     * @param url    sms destination url
     * @return RMIRequest RMI provider http body
     */
    //TODO Method only for mock sendSms service.
    private RMIRequest buildRMIRequest(String text, int prefix, String number, String url) {

        return RMIRequest.builder().text(text).prefix(prefix).number(number).url(url).build();
    }

}
