package com.mobilemessagesgateway.service;

import static com.mobilemessagesgateway.constants.GatewayConstants.ERROR_INVALID_NUMBER;
import static com.mobilemessagesgateway.constants.GatewayConstants.REST_LITERAL;
import static com.mobilemessagesgateway.constants.GatewayConstants.RMI_LITERAL;
import static com.mobilemessagesgateway.constants.GatewayConstants.SOAP_LITERAL;
import static com.mobilemessagesgateway.constants.GatewayConstants.STATUS_ERROR;
import static com.mobilemessagesgateway.constants.GatewayConstants.STATUS_RECEIVED;
import static com.mobilemessagesgateway.constants.GatewayConstants.STATUS_SENT;

import com.mobilemessagesgateway.domain.dto.SmsRequest;
import com.mobilemessagesgateway.domain.dto.SmsResponse;
import com.mobilemessagesgateway.domain.entity.Provider;
import com.mobilemessagesgateway.domain.entity.Sms;
import com.mobilemessagesgateway.domain.repository.SmsRepository;
import com.mobilemessagesgateway.service.sender.RESTService;
import com.mobilemessagesgateway.service.sender.RMIService;
import com.mobilemessagesgateway.service.sender.SOAPService;
import com.mobilemessagesgateway.service.sender.SenderService;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@CommonsLog
public class DispatcherServiceImpl implements DispatcherService {

    NumberUtils numberUtils;
    ProviderService providerService;
    SmsRepository smsRepository;
    SenderService restService;
    SenderService soapService;
    SenderService rmiService;

    @Autowired
    public DispatcherServiceImpl(
            NumberUtils numberUtils, ProviderService providerService, SmsRepository smsRepository,
            RESTService restService, SOAPService soapService, RMIService rmiService) {
        this.numberUtils = numberUtils;
        this.providerService = providerService;
        this.smsRepository = smsRepository;
        this.restService = restService;
        this.soapService = soapService;
        this.rmiService = rmiService;
    }

    /**
     * getSingleProvider
     *
     * @param smsRequests list of SmsRequest object
     * @return List of SmsResponse object after sending the sms
     */
    public List<SmsResponse> sendSms(List<SmsRequest> smsRequests) {
        List<SmsResponse> smsResponses = new ArrayList<>();
        String usedProvider = null;
        Sms sms;
        for (SmsRequest smsRequest : smsRequests) {
            try {
                sms = validateAndPersistNewSmsRequest(smsRequest);
                Provider provider = providerService.findMinCostProvider(smsRequest.getNumber());
                usedProvider = provider.getName();
                send2Provider(sms.getText(), smsRequest.getNumber(), provider.getUrl(), provider.getProtocol());
                persistSentSms(sms, usedProvider);
            } catch (Exception e) {
                sms = Sms.builder().number(smsRequest.getNumber()).text(smsRequest.getText()).status(STATUS_ERROR).build();
                persistErrorSms(sms, e.getMessage());
            }
            smsResponses.add(SmsResponse.builder().id(sms.getId()).provider(usedProvider).status(sms.getStatus()).build());
        }
        return smsResponses;
    }

    /**
     * persistNewSmsRequest
     *
     * @param smsRequest SmsRequest object
     * @return persisted Sms object saved with status RECEIVED
     */
    private Sms validateAndPersistNewSmsRequest(SmsRequest smsRequest) {
        if (smsRequest.getNumber() == null) {
            throw new IllegalArgumentException(ERROR_INVALID_NUMBER + " " + smsRequest.getNumber());
        }
        Sms sms = Sms.builder().number(smsRequest.getNumber()).text(smsRequest.getText()).status(STATUS_RECEIVED).build();
        return smsRepository.save(sms);
    }

    /**
     * send2Provider
     *
     * @param text     message body
     * @param number   phone number
     * @param url      destination url
     * @param protocol chosen protocol
     */
    private void send2Provider(String text, String number, String url, String protocol) {
        switch (protocol) {
            case SOAP_LITERAL -> soapService.sendSms(text, number, url);
            case REST_LITERAL -> restService.sendSms(text, number, url);
            case RMI_LITERAL -> rmiService.sendSms(text, number, url);
        }
    }

    /**
     * persistSentSms
     *
     * @param sms      Sms object
     * @param provider name of the selected provider
     */
    private void persistSentSms(Sms sms, String provider) {
        sms.setStatus(STATUS_SENT);
        sms.setProvider(provider);
        smsRepository.save(sms);
    }

    /**
     * persistErrorSms
     *
     * @param sms     Sms object
     * @param message error message
     */
    private void persistErrorSms(Sms sms, String message) {
        sms.setStatus(STATUS_ERROR);
        sms.setMessage(message);
        smsRepository.save(sms);
    }
}
