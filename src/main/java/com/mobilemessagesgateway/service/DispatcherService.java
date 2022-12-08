package com.mobilemessagesgateway.service;

import com.mobilemessagesgateway.domain.dto.SmsRequest;
import com.mobilemessagesgateway.domain.dto.SmsResponse;
import com.mobilemessagesgateway.domain.entity.Sms;

public interface DispatcherService {

    SmsResponse sendSms(SmsRequest smsRequest);
}
