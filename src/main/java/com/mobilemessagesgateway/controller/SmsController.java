package com.mobilemessagesgateway.controller;

import com.mobilemessagesgateway.domain.repository.ProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SmsController {
    private final ProviderRepository service;

    @Autowired
    public SmsController(ProviderRepository service) {
        this.service = service;
    }

    @GetMapping("/")
    public @ResponseBody Object sendSms() {
        return service.findByPrefixWithMinCost(34);
    }
}
