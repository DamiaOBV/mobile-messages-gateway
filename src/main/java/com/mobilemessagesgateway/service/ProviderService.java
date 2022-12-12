package com.mobilemessagesgateway.service;

import com.mobilemessagesgateway.domain.entity.Provider;

public interface ProviderService {

    Provider findMinCostProvider(String number);
}
