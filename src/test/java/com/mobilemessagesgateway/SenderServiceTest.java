package com.mobilemessagesgateway;

import com.mobilemessagesgateway.service.sender.RESTService;
import com.mobilemessagesgateway.service.sender.RMIService;
import com.mobilemessagesgateway.service.sender.SOAPService;
import lombok.extern.apachecommons.CommonsLog;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
@CommonsLog
@DisplayName("SenderService")
public class SenderServiceTest {

    @Autowired
    RESTService restService;
    @Autowired
    SOAPService soapService;
    @Autowired
    RMIService rmiService;

    @Nested
    @DisplayName("restService")
    class restService {

        @Test
        @DisplayName("Simply test the rest method can be called without failing")
        public void sendSms_CorrectSend_Single() {
            restService.sendSms("text", "number", "url");
        }
    }

    @Nested
    @DisplayName("soapService")
    class soapService {

        @Test
        @DisplayName("Simply test the soap method can be called without failing")
        public void sendSms_CorrectSend_Single() {
            soapService.sendSms("text", "number", "url");
        }
    }

    @Nested
    @DisplayName("rmiService")
    class rmiService {

        @Test
        @DisplayName("Simply test the rmi method can be called without failing")
        public void sendSms_CorrectSend_Single() {
            rmiService.sendSms("text", "number", "url");
        }
    }
}
