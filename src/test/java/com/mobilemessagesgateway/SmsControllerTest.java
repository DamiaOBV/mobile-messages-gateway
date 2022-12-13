package com.mobilemessagesgateway;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.mobilemessagesgateway.domain.dto.SmsRequest;
import com.mobilemessagesgateway.domain.dto.SmsResponse;
import com.mobilemessagesgateway.service.DispatcherService;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DisplayName("SmsController")
//TODO Application functionality at http server level could be better verified in integration testing.
// Implemented as unitary testing in order to keep everything in one application.
public class SmsControllerTest {

    @MockBean
    DispatcherService dispatcherService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String PATH = "/gateway/sms/send";
    private static final String USER = "admin";
    private static final String PASS = "1234";

    @Nested
    @DisplayName("sendSms")
    class sendSms {

        @Nested
        @DisplayName("success")
        class sendSms_Success {

            @Test
            @DisplayName("POST /gateway/sms/send with body request with number attribute should return a 200")
            public void POST_Number_200() {
                SmsResponse smsResponse = SmsResponse.builder().id(1).provider("P1").status("SENT").build();
                List<SmsResponse> smsResponses = new ArrayList<>();
                smsResponses.add(smsResponse);
                when(dispatcherService.sendSms(any())).thenReturn(smsResponses);
                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-Type", "application/json");
                headers.setBasicAuth(USER, PASS);
                SmsRequest smsRequest = SmsRequest.builder().number("132456").text("This is a message").build();
                ResponseEntity<List<SmsResponse>> response = restTemplate.exchange("http://localhost:" + port + "/gateway/sms/send",
                                                                                   HttpMethod.POST, new HttpEntity<>(smsRequest, headers),
                                                                                   new ParameterizedTypeReference<List<SmsResponse>>() {
                                                                                   });
                if (response == null) {
                    Assertions.fail();
                }

                Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
                Assertions.assertNotNull(response.getBody());
                Assertions.assertInstanceOf(SmsResponse.class, response.getBody().get(0));
            }

        }

        @Nested
        @DisplayName("failure")
        class sendSms_Failure {

            @Test
            @DisplayName("GET /gateway/sms/send should return a 405")
            public void GET_405() {
                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-Type", "application/json");
                headers.setBasicAuth(USER, PASS);
                SmsRequest smsRequest = SmsRequest.builder().text("This is a message").build();
                DefaultTestSpringError defaultTestSpringError = restTemplate.exchange("http://localhost:" + port + "/gateway/sms/send",
                                                                                      HttpMethod.GET, new HttpEntity<>(smsRequest, headers),
                                                                                      DefaultTestSpringError.class).getBody();
                if (defaultTestSpringError == null) {
                    Assertions.fail();
                }
                Assertions.assertNotNull(defaultTestSpringError.getTimestamp());
                Assertions.assertEquals(PATH, defaultTestSpringError.getPath());
                Assertions.assertEquals(HttpStatus.METHOD_NOT_ALLOWED.value(), defaultTestSpringError.getStatus());
                Assertions.assertEquals(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase(), defaultTestSpringError.getError());
            }

            @Test
            @DisplayName("POST /gateway/sms/send with html content type should return a 415")
            public void POST_HTMLContent_415() {
                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-Type", "text/html");
                headers.setBasicAuth(USER, PASS);
                DefaultTestSpringError defaultTestSpringError = restTemplate.exchange("http://localhost:" + port + "/gateway/sms/send",
                                                                                      HttpMethod.POST, new HttpEntity<>("html-request", headers),
                                                                                      DefaultTestSpringError.class).getBody();
                if (defaultTestSpringError == null) {
                    Assertions.fail();
                }
                Assertions.assertNotNull(defaultTestSpringError.getTimestamp());
                Assertions.assertEquals(PATH, defaultTestSpringError.getPath());
                Assertions.assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), defaultTestSpringError.getStatus());
                Assertions.assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE.getReasonPhrase(), defaultTestSpringError.getError());
            }

            @Test
            @DisplayName("POST /gateway/sms/send with no body request should return a 400")
            public void POST_NoBody_400() {
                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-Type", "application/json");
                headers.setBasicAuth(USER, PASS);
                DefaultTestSpringError defaultTestSpringError = restTemplate.exchange("http://localhost:" + port + "/gateway/sms/send",
                                                                                      HttpMethod.POST, new HttpEntity<>(headers),
                                                                                      DefaultTestSpringError.class).getBody();
                if (defaultTestSpringError == null) {
                    Assertions.fail();
                }
                Assertions.assertNotNull(defaultTestSpringError.getTimestamp());
                Assertions.assertEquals(PATH, defaultTestSpringError.getPath());
                Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), defaultTestSpringError.getStatus());
                Assertions.assertEquals(HttpStatus.BAD_REQUEST.getReasonPhrase(), defaultTestSpringError.getError());
            }

            @Test
            @DisplayName("POST /gateway/sms/send with body request without number attribute should return a 400")
            public void POST_NoNumber_400() {
                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-Type", "application/json");
                headers.setBasicAuth(USER, PASS);
                SmsRequest smsRequest = SmsRequest.builder().text("This is a message").build();
                DefaultTestSpringError defaultTestSpringError = restTemplate.exchange("http://localhost:" + port + "/gateway/sms/send",
                                                                                      HttpMethod.POST, new HttpEntity<>(smsRequest, headers),
                                                                                      DefaultTestSpringError.class).getBody();
                if (defaultTestSpringError == null) {
                    Assertions.fail();
                }
                Assertions.assertNotNull(defaultTestSpringError.getTimestamp());
                Assertions.assertEquals(PATH, defaultTestSpringError.getPath());
                Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), defaultTestSpringError.getStatus());
                Assertions.assertEquals(HttpStatus.BAD_REQUEST.getReasonPhrase(), defaultTestSpringError.getError());
            }

            @Test
            @DisplayName("POST /gateway/sms/send with incorrect credentials should return a 401")
            public void POST_BadCredentials_401() {
                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-Type", "application/json");
                headers.setBasicAuth(USER, "invalid-password");
                SmsRequest smsRequest = SmsRequest.builder().text("This is a message").build();
                ResponseEntity<DefaultTestSpringError> response = restTemplate.exchange(
                        "http://localhost:" + port + "/gateway/sms/send",
                        HttpMethod.POST, new HttpEntity<>(smsRequest, headers),
                        DefaultTestSpringError.class);
                if (response == null) {
                    Assertions.fail();
                }
                Assertions.assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
            }


        }
    }

    @Data
    private static class DefaultTestSpringError {

        String timestamp;
        int status;
        String error;
        String path;
    }
}
