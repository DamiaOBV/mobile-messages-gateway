package com.mobilemessagesgateway.service;

import static com.mobilemessagesgateway.constants.GatewayConstants.ERROR_INVALID_NUMBER;
import static com.mobilemessagesgateway.constants.GatewayConstants.ERROR_NO_PROVIDERS;
import static com.mobilemessagesgateway.constants.GatewayConstants.REST_LITERAL;
import static com.mobilemessagesgateway.constants.GatewayConstants.RMI_LITERAL;
import static com.mobilemessagesgateway.constants.GatewayConstants.SOAP_LITERAL;
import static com.mobilemessagesgateway.constants.GatewayConstants.STATUS_ERROR;
import static com.mobilemessagesgateway.constants.GatewayConstants.STATUS_RECEIVED;
import static com.mobilemessagesgateway.constants.GatewayConstants.STATUS_SENT;

import com.mobilemessagesgateway.domain.dto.SmsRequest;
import com.mobilemessagesgateway.domain.entity.Provider;
import com.mobilemessagesgateway.domain.entity.Sms;
import com.mobilemessagesgateway.domain.repository.ProviderRepository;
import com.mobilemessagesgateway.domain.repository.SmsRepository;
import com.mobilemessagesgateway.service.sender.RESTService;
import com.mobilemessagesgateway.service.sender.RMIService;
import com.mobilemessagesgateway.service.sender.SOAPService;
import com.mobilemessagesgateway.service.sender.SenderService;
import java.util.Random;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@CommonsLog
public class ProviderServiceImpl implements ProviderService {

    NumberUtils numberUtils;
    ProviderRepository providerRepository;

    @Autowired
    public ProviderServiceImpl(
            NumberUtils numberUtils, ProviderRepository providerRepository) {
        this.numberUtils = numberUtils;
        this.providerRepository = providerRepository;
    }

    /**
     * getSingleProvider
     *
     * @param number phone number with prefix
     * @return chosen Provider with min cost. If there are more than one provider returns one of them randomly
     */
    public Provider findMinCostProvider(String number) {

        String prettyNumber = numberUtils.removeNumLeadingPlusSignAndZeros(number);
        int[] availablePrefixes = providerRepository.getAvailablePrefixes();
        int prefix = numberUtils.getPrefixFromNumber(prettyNumber, availablePrefixes);
        Provider[] providers = providerRepository.findByPrefixWithMinCost(prefix);
        return providers[new Random().nextInt(providers.length)];
    }

}
