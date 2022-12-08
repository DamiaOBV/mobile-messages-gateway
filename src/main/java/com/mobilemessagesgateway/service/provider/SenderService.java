package com.mobilemessagesgateway.service.provider;

import com.mobilemessagesgateway.domain.dto.SmsRequest;

public interface SenderService {
    void sendSms(String text, int prefix, String number, String url);

}
