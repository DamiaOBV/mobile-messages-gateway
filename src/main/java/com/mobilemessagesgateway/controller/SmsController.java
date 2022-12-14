package com.mobilemessagesgateway.controller;

import com.mobilemessagesgateway.domain.dto.SmsRequest;
import com.mobilemessagesgateway.domain.dto.SmsResponse;
import com.mobilemessagesgateway.service.DispatcherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Validated
@RequestMapping(path = "sms")
public class SmsController {

    private final DispatcherService dispatcherService;

    @Autowired
    public SmsController(DispatcherService dispatcherService) {
        this.dispatcherService = dispatcherService;
    }

    @Operation(summary = "Send SMS following Least Cost Routing strategy",
               description = "Send SMS to the minimum cost provider. Input can be a list or a single SmsRequest. If more than one providers have " +
                       "the same cost a random one will be chosen.")
    @ApiResponse(responseCode = "200", description = "OK",
                 content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                     array = @ArraySchema(schema = @Schema(implementation = SmsResponse.class)))})
    @PostMapping("send")
    public ResponseEntity<Object> sendSms(@Valid @RequestBody List<SmsRequest> smsRequest) {
        return ResponseEntity.ok(dispatcherService.sendSms(smsRequest));
    }
}
