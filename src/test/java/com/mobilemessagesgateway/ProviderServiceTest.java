package com.mobilemessagesgateway;

import static com.mobilemessagesgateway.constants.GatewayConstants.ERROR_INVALID_NUMBER;
import static com.mobilemessagesgateway.constants.GatewayConstants.ERROR_PREFIX_NOT_FOUND_FOR_NUMBER;
import static org.mockito.Mockito.when;

import com.mobilemessagesgateway.domain.entity.Provider;
import com.mobilemessagesgateway.domain.repository.ProviderRepository;
import com.mobilemessagesgateway.service.NumberUtils;
import com.mobilemessagesgateway.service.ProviderService;
import java.util.Arrays;
import java.util.stream.Stream;
import lombok.extern.apachecommons.CommonsLog;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@AutoConfigureMockMvc
@CommonsLog
@DisplayName("ProviderService")
public class ProviderServiceTest {

    @Autowired
    ProviderService providerService;

    @MockBean
    ProviderRepository providerRepository;

    @MockBean
    NumberUtils numberUtils;

    static final String[] CORRECT_33 = {"+03312348", "3312348", "33"};
    static final String[] CORRECT_34_1 = {"34123", "34123", "34"};
    static final String[] CORRECT_34_2 = {"+3412345", "3412345", "34"};
    static final String[] CORRECT_35_1 = {"+35123456", "35123456", "35"};
    static final String[] CORRECT_35_2 = {"+3512345", "3512345", "35"};
    static final String[] CORRECT_36_1 = {"003612346", "3612346", "36"};
    static final String[] CORRECT_36_2 = {"+0036123456", "36123456", "36"};

    static final String[] INCORRECT_NO_PREFIX_1 = {"40123", "40123"};
    static final String[] INCORRECT_NO_PREFIX_3 = {"+00123456", "123456"};
    static final String[] INCORRECT_NO_PREFIX_4 = {"+30123456", "30123456"};
    static final String[] INCORRECT_NO_PREFIX_2 = {"+01212348", "1212348"};

    static final String INCORRECT_NAN_1 = "xczc";
    static final String INCORRECT_NAN_2 = "+0asdasd";
    static final String INCORRECT_NAN_3 = "++++//(/)$";
    static final String INCORRECT_NAN_4 = "+s12/*/*";

    static final int[] PREFIXES = {33, 34, 35, 36};

    @BeforeEach
    public void init(@Autowired ProviderRepository providerRepository) {

        Provider[] providers33 = {
                Provider.builder().id(4L).cost(3).prefix(33).protocol("REST").name("P5").url("jkl").build()
        };
        Provider[] providers34 = {
                Provider.builder().id(1L).cost(1).prefix(34).protocol("REST").name("P6").url("abc").build(),
                Provider.builder().id(2L).cost(1).prefix(34).protocol("RMI").name("P7").url("ghi").build()
        };
        Provider[] providers35 = {
                Provider.builder().id(1L).cost(2).prefix(35).protocol("RMI").name("P8").url("abc").build(),
                Provider.builder().id(2L).cost(2).prefix(35).protocol("RMI").name("P9").url("ghi").build(),
                Provider.builder().id(2L).cost(2).prefix(35).protocol("REST").name("P10").url("ghi").build()
        };
        Provider[] providers36 = {
                Provider.builder().id(1L).cost(2).prefix(36).protocol("SOAP").name("P11").url("abc").build(),
                Provider.builder().id(2L).cost(2).prefix(36).protocol("RMI").name("P12").url("ghi").build(),
                Provider.builder().id(2L).cost(2).prefix(36).protocol("SOAP").name("P13").url("ghi").build(),
                Provider.builder().id(2L).cost(2).prefix(36).protocol("REST").name("P14").url("ghi").build()
        };
        when(providerRepository.getAvailablePrefixes()).thenReturn(PREFIXES);
        when(providerRepository.findByPrefixWithMinCost(33)).thenReturn(providers33);
        when(providerRepository.findByPrefixWithMinCost(34)).thenReturn(providers34);
        when(providerRepository.findByPrefixWithMinCost(35)).thenReturn(providers35);
        when(providerRepository.findByPrefixWithMinCost(36)).thenReturn(providers36);
        when(numberUtils.removeNumLeadingPlusSignAndZeros(CORRECT_33[0])).thenReturn(CORRECT_33[1]);
        when(numberUtils.getPrefixFromNumber(CORRECT_33[1], PREFIXES)).thenReturn(Integer.parseInt(CORRECT_33[2]));
        when(numberUtils.removeNumLeadingPlusSignAndZeros(CORRECT_34_1[0])).thenReturn(CORRECT_34_1[1]);
        when(numberUtils.getPrefixFromNumber(CORRECT_34_1[1], PREFIXES)).thenReturn(Integer.parseInt(CORRECT_34_1[2]));
        when(numberUtils.removeNumLeadingPlusSignAndZeros(CORRECT_34_2[0])).thenReturn(CORRECT_34_2[1]);
        when(numberUtils.getPrefixFromNumber(CORRECT_34_2[1], PREFIXES)).thenReturn(Integer.parseInt(CORRECT_34_2[2]));
        when(numberUtils.removeNumLeadingPlusSignAndZeros(CORRECT_35_1[0])).thenReturn(CORRECT_35_1[1]);
        when(numberUtils.getPrefixFromNumber(CORRECT_35_1[1], PREFIXES)).thenReturn(Integer.parseInt(CORRECT_35_1[2]));
        when(numberUtils.removeNumLeadingPlusSignAndZeros(CORRECT_35_2[0])).thenReturn(CORRECT_35_2[1]);
        when(numberUtils.getPrefixFromNumber(CORRECT_35_2[1], PREFIXES)).thenReturn(Integer.parseInt(CORRECT_35_2[2]));
        when(numberUtils.removeNumLeadingPlusSignAndZeros(CORRECT_36_1[0])).thenReturn(CORRECT_36_1[1]);
        when(numberUtils.getPrefixFromNumber(CORRECT_36_1[1], PREFIXES)).thenReturn(Integer.parseInt(CORRECT_36_1[2]));
        when(numberUtils.removeNumLeadingPlusSignAndZeros(CORRECT_36_2[0])).thenReturn(CORRECT_36_2[1]);
        when(numberUtils.getPrefixFromNumber(CORRECT_36_2[1], PREFIXES)).thenReturn(Integer.parseInt(CORRECT_36_2[2]));

        when(numberUtils.removeNumLeadingPlusSignAndZeros(INCORRECT_NO_PREFIX_1[0])).thenReturn(INCORRECT_NO_PREFIX_1[1]);
        when(numberUtils.getPrefixFromNumber(INCORRECT_NO_PREFIX_1[1], PREFIXES)).thenThrow(
                new IllegalArgumentException(ERROR_PREFIX_NOT_FOUND_FOR_NUMBER + " " + INCORRECT_NO_PREFIX_1[1] + " " + Arrays.toString(PREFIXES)));
        when(numberUtils.removeNumLeadingPlusSignAndZeros(INCORRECT_NO_PREFIX_2[0])).thenReturn(INCORRECT_NO_PREFIX_2[1]);
        when(numberUtils.getPrefixFromNumber(INCORRECT_NO_PREFIX_2[1], PREFIXES)).thenThrow(
                new IllegalArgumentException(ERROR_PREFIX_NOT_FOUND_FOR_NUMBER + " " + INCORRECT_NO_PREFIX_2[1] + " " + Arrays.toString(PREFIXES)));
        when(numberUtils.removeNumLeadingPlusSignAndZeros(INCORRECT_NO_PREFIX_3[0])).thenReturn(INCORRECT_NO_PREFIX_3[1]);
        when(numberUtils.getPrefixFromNumber(INCORRECT_NO_PREFIX_3[1], PREFIXES)).thenThrow(
                new IllegalArgumentException(ERROR_PREFIX_NOT_FOUND_FOR_NUMBER + " " + INCORRECT_NO_PREFIX_3[1] + " " + Arrays.toString(PREFIXES)));
        when(numberUtils.removeNumLeadingPlusSignAndZeros(INCORRECT_NO_PREFIX_4[0])).thenReturn(INCORRECT_NO_PREFIX_4[1]);
        when(numberUtils.getPrefixFromNumber(INCORRECT_NO_PREFIX_4[1], PREFIXES)).thenThrow(
                new IllegalArgumentException(ERROR_PREFIX_NOT_FOUND_FOR_NUMBER + " " + INCORRECT_NO_PREFIX_4[1] + " " + Arrays.toString(PREFIXES)));

        when(numberUtils.removeNumLeadingPlusSignAndZeros(INCORRECT_NAN_1)).thenThrow(
                new IllegalArgumentException(ERROR_INVALID_NUMBER + " " + INCORRECT_NAN_1));
        when(numberUtils.removeNumLeadingPlusSignAndZeros(INCORRECT_NAN_2)).thenThrow(
                new IllegalArgumentException(ERROR_INVALID_NUMBER + " " + INCORRECT_NAN_2));
        when(numberUtils.removeNumLeadingPlusSignAndZeros(INCORRECT_NAN_3)).thenThrow(
                new IllegalArgumentException(ERROR_INVALID_NUMBER + " " + INCORRECT_NAN_3));
        when(numberUtils.removeNumLeadingPlusSignAndZeros(INCORRECT_NAN_4)).thenThrow(
                new IllegalArgumentException(ERROR_INVALID_NUMBER + " " + INCORRECT_NAN_4));
    }

    @Nested
    @DisplayName("findMinCostProvider")
    class findMinCostProvider {

        @Nested
        @DisplayName("success")
        class findMinCostProvider_Success {

            @ParameterizedTest
            @MethodSource("generateFindMinCostProviderValidData")
            @DisplayName("findMinCostProvider with a number with an existing prefix should return a Provider object")
            public void findMinCostProvider_ProviderRetrieved(String number) {
                Assertions.assertInstanceOf(Provider.class, providerService.findMinCostProvider(number));
            }

            private static Stream<Arguments> generateFindMinCostProviderValidData() {
                return Stream.of(Arguments.of(CORRECT_33[0]),
                                 Arguments.of(CORRECT_34_1[0]),
                                 Arguments.of(CORRECT_34_2[0]),
                                 Arguments.of(CORRECT_35_1[0]),
                                 Arguments.of(CORRECT_35_2[0]),
                                 Arguments.of(CORRECT_36_1[0]),
                                 Arguments.of(CORRECT_36_2[0]));
            }

            @Nested
            @DisplayName("randomness")
            class findMinCostProvider_Randomness {

                @Test
                @DisplayName("findMinCostProvider when there are 2 providers with minimum cost a random provider should be used")
                public void findMinCostProvider_Randomness2() {
                    double minPercent = 40;
                    double maxPercent = 60;
                    int iterations = 300;
                    double providerP6 = 0;
                    double providerP7 = 0;
                    Provider provider;
                    String providerName;
                    for (int i = 0; i < iterations; i++) {
                        provider = providerService.findMinCostProvider(CORRECT_34_2[0]);
                        providerName = provider.getName();
                        switch (providerName) {
                            case "P6" -> providerP6++;
                            case "P7" -> providerP7++;
                        }
                    }
                    double percentP6 = (providerP6 / iterations) * 100;
                    double percentP7 = (providerP7 / iterations) * 100;
                    log.info("providerP6: " + percentP6);
                    log.info("providerP7: " + percentP7);
                    Assertions.assertTrue(percentP6 > minPercent && percentP6 < maxPercent && percentP7 > minPercent && percentP7 < maxPercent);
                }

                @Test
                @DisplayName("findMinCostProvider when there are 3 provider with minimum cost a random provider should be used")
                public void findMinCostProvider_Randomness3() {
                    double minPercent = 15;
                    double maxPercent = 45;
                    int iterations = 300;
                    double providerP8 = 0;
                    double providerP9 = 0;
                    double providerP10 = 0;
                    Provider provider;
                    String providerName;
                    for (int i = 0; i < iterations; i++) {
                        provider = providerService.findMinCostProvider(CORRECT_35_2[0]);
                        providerName = provider.getName();
                        switch (providerName) {
                            case "P8" -> providerP8++;
                            case "P9" -> providerP9++;
                            case "P10" -> providerP10++;
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
                @DisplayName("findMinCostProvider when there are 4 provider with minimum cost a random provider should be used")
                public void findMinCostProvider_Randomness4() {
                    double minPercent = 10;
                    double maxPercent = 35;
                    int iterations = 100;
                    double providerP11 = 0;
                    double providerP12 = 0;
                    double providerP13 = 0;
                    double providerP14 = 0;

                    Provider provider;
                    String providerName;
                    for (int i = 0; i < iterations; i++) {
                        provider = providerService.findMinCostProvider(CORRECT_36_1[0]);
                        providerName = provider.getName();
                        switch (providerName) {
                            case "P11" -> providerP11++;
                            case "P12" -> providerP12++;
                            case "P13" -> providerP13++;
                            case "P14" -> providerP14++;
                        }
                    }
                    double percentP11 = (providerP11 / (iterations)) * 100;
                    double percentP12 = (providerP12 / (iterations)) * 100;
                    double percentP13 = (providerP13 / (iterations)) * 100;
                    double percentP14 = (providerP14 / (iterations)) * 100;
                    log.info("providerP8: " + percentP11);
                    log.info("providerP9: " + percentP12);
                    log.info("providerP10: " + percentP13);
                    log.info("providerP10: " + percentP14);
                    Assertions.assertTrue(percentP11 > minPercent && percentP11 < maxPercent && percentP12 > minPercent && percentP12 < maxPercent &&
                                                  percentP13 > minPercent && percentP13 < maxPercent && percentP14 > minPercent &&
                                                  percentP14 < maxPercent);
                }
            }
        }

        @Nested
        @DisplayName("failure")
        class findMinCostProvider_Failure {

            @ParameterizedTest
            @MethodSource("generateFindMinCostProviderInvalidNoPrefixData")
            @DisplayName("findMinCostProvider with a number with a non existing prefix should return IllegalArgumentException")
            public void findMinCostProvider_IllegalArgumentException(String number, String prettyNumber) {
                Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> providerService.findMinCostProvider(number));
                Assertions.assertEquals((ERROR_PREFIX_NOT_FOUND_FOR_NUMBER + " " + prettyNumber + " " + Arrays.toString(PREFIXES)), exception.getMessage());
            }

            private static Stream<Arguments> generateFindMinCostProviderInvalidNoPrefixData() {
                return Stream.of(Arguments.of(INCORRECT_NO_PREFIX_1[0],INCORRECT_NO_PREFIX_1[1]),
                                 Arguments.of(INCORRECT_NO_PREFIX_2[0],INCORRECT_NO_PREFIX_2[1]),
                                 Arguments.of(INCORRECT_NO_PREFIX_3[0],INCORRECT_NO_PREFIX_3[1]),
                                 Arguments.of(INCORRECT_NO_PREFIX_4[0],INCORRECT_NO_PREFIX_4[1]));
            }

            @ParameterizedTest
            @MethodSource("generateFindMinCostProviderInvalidNaNData")
            @DisplayName("findMinCostProvider with a non numeric number argument should return IllegalArgumentException")
            public void findMinCostProvider_atrieved(String number) {
                Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> providerService.findMinCostProvider(number));
                Assertions.assertEquals((ERROR_INVALID_NUMBER + " " + number), exception.getMessage());
            }

            private static Stream<Arguments> generateFindMinCostProviderInvalidNaNData() {
                return Stream.of(Arguments.of(INCORRECT_NAN_1),
                                 Arguments.of(INCORRECT_NAN_2),
                                 Arguments.of(INCORRECT_NAN_3),
                                 Arguments.of(INCORRECT_NAN_4));
            }
        }


    }
}
