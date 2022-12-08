package com.mobilemessagesgateway.service;

import static com.mobilemessagesgateway.constants.GatewayConstants.ERROR_INVALID_NUMBER;
import static com.mobilemessagesgateway.constants.GatewayConstants.ERROR_NO_PROVIDERS;
import static com.mobilemessagesgateway.constants.GatewayConstants.STATUS_RECEIVED;
import static com.mobilemessagesgateway.constants.GatewayConstants.STATUS_SENT;

import com.mobilemessagesgateway.domain.dto.SmsRequest;
import com.mobilemessagesgateway.domain.dto.SmsResponse;
import com.mobilemessagesgateway.domain.entity.Provider;
import com.mobilemessagesgateway.domain.entity.Sms;
import com.mobilemessagesgateway.domain.repository.ProviderRepository;
import com.mobilemessagesgateway.domain.repository.SmsRepository;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DispatcherServiceImpl implements DispatcherService {

    NumberPrefixService numberPrefixService;
    ProviderRepository providerRepository;
    SmsRepository smsRepository;

    @Autowired
    public DispatcherServiceImpl(NumberPrefixService numberPrefixService, ProviderRepository providerRepository, SmsRepository smsRepository) {
        this.numberPrefixService = numberPrefixService;
        this.providerRepository = providerRepository;
        this.smsRepository = smsRepository;
    }

    /**
     * getSingleProvider
     *
     * @param smsRequest SmsRequest object
     * @return SmsResponse object after sending the sms
     */
    public SmsResponse sendSms(SmsRequest smsRequest) {
        Sms sms = validateAndPersistNewSmsRequest(smsRequest);
        String prettyNumber = numberPrefixService.removeLeadingPlusSignAndZeros(smsRequest.getNumber());
        int[] availablePrefixes = providerRepository.getAvailablePrefixes();
        int prefix = numberPrefixService.getPrefixFromNumber(prettyNumber, availablePrefixes);
        Provider[] providers = providerRepository.findByPrefixWithMinCost(prefix);
        Provider provider = getSingleProvider(providers);
        //Enviar missatge
        persistSentSms(sms, provider.getName());
        return SmsResponse.builder().id(sms.getId()).provider(provider.getName()).build();
    }

    /**
     * getSingleProvider
     *
     * @param providers array of providers
     * @return random provider from the array input
     * @throws NullPointerException if providers is null
     * @throws IllegalArgumentException if providers length is zero
     */
    private Provider getSingleProvider(Provider[] providers) {
        if (providers == null ||providers.length == 0) {
            throw new IllegalArgumentException(ERROR_NO_PROVIDERS);
        } else if (providers.length == 1) {
            return providers[0];
        } else {
            return providers[new Random().nextInt(providers.length)];
        }
    }

    /**
     * persistNewSmsRequest
     *
     * @param smsRequest SmsRequest object
     * @return persisted Sms object saved with status RECEIVED
     */
    private Sms validateAndPersistNewSmsRequest(SmsRequest smsRequest) {
        if(smsRequest.getNumber() == null){
            throw new IllegalArgumentException(ERROR_INVALID_NUMBER + " " + smsRequest.getNumber());
        }
        Sms sms = Sms.builder().number(smsRequest.getNumber()).text(smsRequest.getText()).status(STATUS_RECEIVED).build();
        return smsRepository.save(sms);
    }

    /**
     * updateSmsStatus
     *
     * @param sms Sms object
     * @param provider name of the selected provider
     */
    private void persistSentSms(Sms sms, String provider) {
        sms.setStatus(STATUS_SENT);
        sms.setProvider(provider);
        smsRepository.save(sms);
    }

}
