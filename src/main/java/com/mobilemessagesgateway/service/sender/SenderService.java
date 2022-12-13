package com.mobilemessagesgateway.service.sender;

public interface SenderService {

    void sendSms(String text, String number, String url);

}
