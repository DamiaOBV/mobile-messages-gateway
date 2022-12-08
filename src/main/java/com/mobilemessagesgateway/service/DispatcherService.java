package com.mobilemessagesgateway.service;

import com.mobilemessagesgateway.domain.dto.SmsRequest;
import com.mobilemessagesgateway.domain.dto.SmsResponse;
import com.mobilemessagesgateway.domain.entity.Sms;
import java.util.List;

public interface DispatcherService {

    List<SmsResponse> sendSms(List<SmsRequest> smsRequests);
}
