package com.mobilemessagesgateway;

import static com.mobilemessagesgateway.constants.GatewayConstants.ERROR_INVALID_NUMBER;
import static com.mobilemessagesgateway.constants.GatewayConstants.ERROR_PREFIX_NOT_FOUND_FOR_NUMBER;
import static org.mockito.Mockito.when;

import com.mobilemessagesgateway.domain.dto.SmsRequest;
import com.mobilemessagesgateway.domain.dto.SmsResponse;
import com.mobilemessagesgateway.domain.entity.Provider;
import com.mobilemessagesgateway.domain.repository.ProviderRepository;
import com.mobilemessagesgateway.service.DispatcherService;
import lombok.extern.apachecommons.CommonsLog;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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
    ProviderRepository providerRepository;

    @BeforeEach
    public void init(@Autowired ProviderRepository providerRepository) {
        int[] prefixes = {33, 34, 35, 36};
        Provider[] providers33 = {
                Provider.builder().id(4L).cost(3).prefix(33).protocol("REST").name("P5").url("jkl").build()
        };
        Provider[] providers34 = {
                Provider.builder().id(1L).cost(1).prefix(34).protocol("REST").name("P6").url("abc").build(),
                Provider.builder().id(2L).cost(1).prefix(34).protocol("REST").name("P7").url("ghi").build()
                //                Provider.builder().id(3L).cost(2).prefix(34).protocol("SOAP").name("P2").url("def").build()
        };
        Provider[] providers35 = {
                Provider.builder().id(1L).cost(2).prefix(35).protocol("REST").name("P8").url("abc").build(),
                Provider.builder().id(2L).cost(2).prefix(35).protocol("REST").name("P9").url("ghi").build(),
                Provider.builder().id(2L).cost(2).prefix(35).protocol("REST").name("P10").url("ghi").build()
                //                Provider.builder().id(3L).cost(2).prefix(34).protocol("SOAP").name("P2").url("def").build()
        };
        Provider[] providers36 = {
                Provider.builder().id(1L).cost(2).prefix(36).protocol("REST").name("P11").url("abc").build(),
                Provider.builder().id(2L).cost(2).prefix(36).protocol("REST").name("P12").url("ghi").build(),
                Provider.builder().id(2L).cost(2).prefix(36).protocol("REST").name("P13").url("ghi").build(),
                Provider.builder().id(2L).cost(2).prefix(36).protocol("REST").name("P14").url("ghi").build()
                //                Provider.builder().id(3L).cost(2).prefix(34).protocol("SOAP").name("P2").url("def").build()
        };
        when(providerRepository.getAvailablePrefixes()).thenReturn(prefixes);
        when(providerRepository.findByPrefixWithMinCost(33)).thenReturn(providers33);
        when(providerRepository.findByPrefixWithMinCost(34)).thenReturn(providers34);
        when(providerRepository.findByPrefixWithMinCost(35)).thenReturn(providers35);
        when(providerRepository.findByPrefixWithMinCost(36)).thenReturn(providers36);
    }

    @Nested
    @DisplayName("sendSms")
    class sendSms {

        @Nested
        @DisplayName("success")
        class sendSms_Success {

            @Test
            @DisplayName("sendSms with a correct input should return an object instace of SmsResponse")
            public void sendSms_CorrectSend() {
                SmsRequest smsRequest = SmsRequest.builder().text("This is a message").number("3412345").build();
                SmsResponse smsResponse = dispatcherService.sendSms(smsRequest);
                log.info("provider: " + smsResponse.getProvider());
                log.info("id: " + smsResponse.getId());
                Assertions.assertInstanceOf(SmsResponse.class, smsResponse);
            }

            @Test
            @DisplayName("sendSms when there are two providers with minimum cost a random provider should be used")
            public void sendSms_Randomness2() {
                double minPercent = 40;
                double maxPercent = 60;
                int iterations = 100;
                double providerP6 = 0;
                double providerP7 = 0;
                SmsRequest smsRequest = SmsRequest.builder().text("This is a message").number("3412345").build();
                SmsResponse smsResponse;
                String provider;
                for (int i = 0; i < iterations; i++) {
                    smsResponse = dispatcherService.sendSms(smsRequest);
                    provider = smsResponse.getProvider();
                    if (provider.equals("P6")) {
                        providerP6++;
                    } else if (provider.equals("P7")) {
                        providerP7++;
                    }
                }
                double percentP6 = (providerP6 / iterations) * 100;
                double percentP7 = (providerP7 / iterations) * 100;
                log.info("providerP6: " + percentP6);
                log.info("providerP7: " + percentP7);
                Assertions.assertTrue(percentP6 > minPercent && percentP6 < maxPercent && percentP7 > minPercent && percentP7 < maxPercent);
            }

            @Test
            @DisplayName("sendSms when there are three provider with minimum cost a random provider should be used")
            public void sendSms_Randomness3() {
                double minPercent = 15;
                double maxPercent = 45;
                int iterations = 100;
                double providerP8 = 0;
                double providerP9 = 0;
                double providerP10 = 0;
                SmsRequest smsRequest = SmsRequest.builder().text("This is a message").number("3512345").build();
                SmsResponse smsResponse;
                String provider;
                for (int i = 0; i < iterations; i++) {
                    smsResponse = dispatcherService.sendSms(smsRequest);
                    provider = smsResponse.getProvider();
                    if (provider.equals("P8")) {
                        providerP8++;
                    } else if (provider.equals("P9")) {
                        providerP9++;
                    } else if (provider.equals("P10")) {
                        providerP10++;
                    }
                }
                double percentP8 = (providerP8 / iterations) * 100;
                double percentP9 = (providerP9 / iterations) * 100;
                double percentP10 = (providerP10 / iterations) * 100;
                log.info("providerP8: " + percentP8);
                log.info("providerP9: " + percentP9);
                log.info("providerP10: " + percentP10);
                Assertions.assertTrue(percentP8 > minPercent && percentP8 < maxPercent && percentP9 > minPercent && percentP9 < maxPercent &&
                                              percentP10 > minPercent && percentP10 < maxPercent);
            }

            @Test
            @DisplayName("sendSms when there are four provider with minimum cost a random provider should be used")
            public void sendSms_Randomness4() {
                double minPercent = 10;
                double maxPercent = 35;
                int iterations = 100;
                double providerP11 = 0;
                double providerP12 = 0;
                double providerP13 = 0;
                double providerP14 = 0;
                SmsRequest smsRequest = SmsRequest.builder().text("This is a message").number("3612345").build();
                SmsResponse smsResponse;
                String provider;
                for (int i = 0; i < iterations; i++) {
                    smsResponse = dispatcherService.sendSms(smsRequest);
                    provider = smsResponse.getProvider();
                    if (provider.equals("P11")) {
                        providerP11++;
                    } else if (provider.equals("P12")) {
                        providerP12++;
                    } else if (provider.equals("P13")) {
                        providerP13++;
                    } else if (provider.equals("P14")) {
                        providerP14++;
                    }
                }
                double percentP11 = (providerP11 / iterations) * 100;
                double percentP12 = (providerP12 / iterations) * 100;
                double percentP13 = (providerP13 / iterations) * 100;
                double percentP14 = (providerP14 / iterations) * 100;
                log.info("providerP8: " + percentP11);
                log.info("providerP9: " + percentP12);
                log.info("providerP10: " + percentP13);
                log.info("providerP10: " + percentP14);
                Assertions.assertTrue(percentP11 > minPercent && percentP11 < maxPercent && percentP12 > minPercent && percentP12 < maxPercent &&
                                              percentP13 > minPercent && percentP13 < maxPercent && percentP14 > minPercent &&
                                              percentP14 < maxPercent);
            }
        }

        @Nested
        @DisplayName("failure")
        class sendSms_Failure {

            @Test
            @DisplayName("sendSms with not numeric number argument should throw IllegalArgumentException")
            public void sendSms_IllegalArgumentException_NaN() {
                SmsRequest smsRequest = SmsRequest.builder().text("This is a message").number("dasd&%").build();
                Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
                    dispatcherService.sendSms(smsRequest);
                });
                Assertions.assertEquals(ERROR_INVALID_NUMBER + " " + smsRequest.getNumber(), exception.getMessage());
            }

            @Test
            @DisplayName("sendSms with null number argument should throw IllegalArgumentException")
            public void sendSms_IllegalArgumentException_Null() {
                SmsRequest smsRequest = SmsRequest.builder().text("This is a message").number(null).build();
                Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
                    dispatcherService.sendSms(smsRequest);
                });
                Assertions.assertEquals(ERROR_INVALID_NUMBER + " " + smsRequest.getNumber(), exception.getMessage());
            }

            @Test
            @DisplayName("sendSms with an unknown prefix number should throw IllegalArgumentException")
            public void sendSms_IllegalArgumentException_NaaN() {
                SmsRequest smsRequest = SmsRequest.builder().text("This is a message").number("+31666456789").build();
                Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
                    dispatcherService.sendSms(smsRequest);
                });
                Assertions.assertTrue(exception.getMessage().startsWith(ERROR_PREFIX_NOT_FOUND_FOR_NUMBER));
            }
        }
    }
}
