package com.mobilemessagesgateway;

import static com.mobilemessagesgateway.constants.GatewayConstants.ERROR_INVALID_NUMBER;
import static com.mobilemessagesgateway.constants.GatewayConstants.ERROR_PREFIX_NOT_FOUND_FOR_NUMBER;
import static com.mobilemessagesgateway.constants.GatewayConstants.STATUS_ERROR;
import static com.mobilemessagesgateway.constants.GatewayConstants.STATUS_SENT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.mobilemessagesgateway.domain.dto.SmsRequest;
import com.mobilemessagesgateway.domain.dto.SmsResponse;
import com.mobilemessagesgateway.domain.entity.Provider;
import com.mobilemessagesgateway.service.DispatcherService;
import com.mobilemessagesgateway.service.ProviderService;
import com.mobilemessagesgateway.service.sender.RESTService;
import com.mobilemessagesgateway.service.sender.RMIService;
import com.mobilemessagesgateway.service.sender.SOAPService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.extern.apachecommons.CommonsLog;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@AutoConfigureMockMvc
@CommonsLog
@DisplayName("DispatcherService")
public class DispatcherServiceTest {

    @Autowired
    DispatcherService dispatcherService;

    @MockBean
    ProviderService providerService;

    @MockBean
    RESTService restService;
    @MockBean
    SOAPService soapService;
    @MockBean
    RMIService rmiService;

    static final String CORRECT_33 = "3312345";
    static final String CORRECT_34 = "+3412345";
    static final String CORRECT_35 = "003512345";
    static final String INCORRECT_40 = "4012345";
    static final String INCORRECT_STR = "dasd&%";
    static final String INCORRECT_UNKNOWN = "+31666456789";

    @BeforeEach
    public void init() {
        int[] prefixes = {33, 34, 35, 36};
        when(providerService.findMinCostProvider(CORRECT_33)).thenReturn(
                Provider.builder().id(4L).cost(3).prefix(33).protocol("REST").name("P5").url("jkl").build());
        when(providerService.findMinCostProvider(CORRECT_34)).thenReturn(
                Provider.builder().id(1L).cost(1).prefix(34).protocol("SOAP").name("P6").url("abc").build());
        when(providerService.findMinCostProvider(CORRECT_35)).thenReturn(
                Provider.builder().id(1L).cost(1).prefix(35).protocol("RMI").name("P6").url("def").build());
        when(providerService.findMinCostProvider(INCORRECT_40)).thenThrow(
                new IllegalArgumentException(ERROR_PREFIX_NOT_FOUND_FOR_NUMBER + " " + INCORRECT_40 + " " + Arrays.toString(prefixes)));
        when(providerService.findMinCostProvider(INCORRECT_STR)).thenThrow(new IllegalArgumentException(ERROR_INVALID_NUMBER + " " + INCORRECT_STR));
        when(providerService.findMinCostProvider(null)).thenThrow(new IllegalArgumentException(ERROR_INVALID_NUMBER + " " + null));
        when(providerService.findMinCostProvider(INCORRECT_UNKNOWN)).thenThrow(
                new IllegalArgumentException(ERROR_PREFIX_NOT_FOUND_FOR_NUMBER + " " + INCORRECT_UNKNOWN + " " + Arrays.toString(prefixes)));
    }

    @Nested
    @DisplayName("sendSms")
    class sendSms {

        @Nested
        @DisplayName("success")
        class sendSms_Success {

            @Test
            @DisplayName("sendSms with a correct single input should return a one element list of SmsResponse with SENT status")
            public void sendSms_CorrectSend_Single() {
                List<SmsRequest> smsRequests = new ArrayList<>();
                smsRequests.add(SmsRequest.builder().text("This is a message").number(CORRECT_34).build());
                List<SmsResponse> smsResponses = dispatcherService.sendSms(smsRequests);
                Mockito.verify(soapService).sendSms(any(), any(), any());
                Assertions.assertTrue(STATUS_SENT.equalsIgnoreCase(smsResponses.get(0).getStatus()));
            }

            @Test
            @DisplayName("sendSms with correct and incorrect input should return a SmsResponse List indicating which messages have been sent")
            public void sendSms_CorrectSend() {
                List<SmsRequest> smsRequests = new ArrayList<>();
                smsRequests.add(SmsRequest.builder().text("This is a message").number(CORRECT_33).build());
                smsRequests.add(SmsRequest.builder().text("This is a message2").number(CORRECT_35).build());
                smsRequests.add(SmsRequest.builder().text("This is a message3").number("4012345").build());
                List<SmsResponse> smsResponses = dispatcherService.sendSms(smsRequests);
                Mockito.verify(restService).sendSms(any(), any(), any());
                Mockito.verify(rmiService).sendSms(any(), any(), any());
                Assertions.assertTrue(STATUS_SENT.equalsIgnoreCase(smsResponses.get(0).getStatus()) &&
                                              STATUS_SENT.equalsIgnoreCase(smsResponses.get(1).getStatus()) &&
                                              STATUS_ERROR.equalsIgnoreCase(smsResponses.get(2).getStatus()));
            }
        }

        @Nested
        @DisplayName("failure")
        class sendSms_Failure {

            @Test
            @DisplayName("sendSms with not numeric number argument should return SmsResponse with error status")
            public void sendSms_IllegalArgumentException_NaN() {
                List<SmsRequest> smsRequests = new ArrayList<>();
                smsRequests.add(SmsRequest.builder().text("This is a message").number(INCORRECT_STR).build());
                List<SmsResponse> smsResponses = dispatcherService.sendSms(smsRequests);
                Assertions.assertTrue(STATUS_ERROR.equalsIgnoreCase(smsResponses.get(0).getStatus()));
            }

            @Test
            @DisplayName("sendSms with null number argument should return SmsResponse with error status")
            public void sendSms_IllegalArgumentException_Null() {
                List<SmsRequest> smsRequests = new ArrayList<>();
                smsRequests.add(SmsRequest.builder().text("This is a message").number(null).build());
                List<SmsResponse> smsResponses = dispatcherService.sendSms(smsRequests);
                Assertions.assertTrue(STATUS_ERROR.equalsIgnoreCase(smsResponses.get(0).getStatus()));
            }

            @Test
            @DisplayName("sendSms with an unknown prefix number should return SmsResponse with error status")
            public void sendSms_IllegalArgumentException_NaaN() {
                List<SmsRequest> smsRequests = new ArrayList<>();
                smsRequests.add(SmsRequest.builder().text("This is a message").number("+31666456789").build());
                List<SmsResponse> smsResponses = dispatcherService.sendSms(smsRequests);
                Assertions.assertTrue(STATUS_ERROR.equalsIgnoreCase(smsResponses.get(0).getStatus()));
            }
        }
    }
}
