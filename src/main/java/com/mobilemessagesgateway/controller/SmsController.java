package com.mobilemessagesgateway.controller;

import com.mobilemessagesgateway.domain.dto.SmsRequest;
import com.mobilemessagesgateway.domain.dto.SmsResponse;
import com.mobilemessagesgateway.service.DispatcherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "sms")
public class SmsController {

    private final DispatcherService dispatcherService;

    @Autowired
    public SmsController(DispatcherService dispatcherService) {
        this.dispatcherService = dispatcherService;
    }

    @Operation(summary = "Send SMS",
               description = "Send SMS following Least Cost Routing strategy")
    @ApiResponse(responseCode = "200", description = "Correct response",
                 content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                     schema = @Schema(implementation = SmsResponse.class))})
    @PostMapping("send")
    public @ResponseBody Object sendSms(@RequestBody List<SmsRequest> smsRequest) {
        return dispatcherService.sendSms(smsRequest);
    }
}
