package com.mobilemessagesgateway.service;

import com.mobilemessagesgateway.domain.entity.Provider;
import com.mobilemessagesgateway.domain.repository.ProviderRepository;
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
     * Returns the minimum cost provider for a given number if exists for the number prefix.
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
