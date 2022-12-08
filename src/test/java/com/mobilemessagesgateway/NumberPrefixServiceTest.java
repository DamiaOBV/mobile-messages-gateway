package com.mobilemessagesgateway;

import static com.mobilemessagesgateway.constants.GatewayConstants.ERROR_INVALID_NUMBER;
import static com.mobilemessagesgateway.constants.GatewayConstants.ERROR_NO_PREFIXES;
import static com.mobilemessagesgateway.constants.GatewayConstants.ERROR_REMOVE_LEAD_PLUS_0_NULL_INPUT;

import com.mobilemessagesgateway.service.NumberPrefixService;
import java.util.stream.Stream;
import lombok.extern.apachecommons.CommonsLog;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
@CommonsLog
@DisplayName("ProviderService")
public class NumberPrefixServiceTest {

    @Autowired
    NumberPrefixService numberPrefixService;

    @Nested
    @DisplayName("removeLeadingPlusSignAndZeros")
    class removeLeadingPlusSignAndZeros {

        @Nested
        @DisplayName("failure")
        class removeLeadingPlusSignAndZeros_Failure {

            @Test
            @DisplayName("removeLeadingPlusSignAndZeros with null input should throw IllegalArgumentException")
            public void removeLeadingPlusSignAndZeros_IllegalArgumentException() {
                Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
                    numberPrefixService.removeLeadingPlusSignAndZeros(null);
                });
                Assertions.assertEquals(ERROR_REMOVE_LEAD_PLUS_0_NULL_INPUT, exception.getMessage());
            }
        }

        @Nested
        @DisplayName("success")
        class removeLeadingPlusSignAndZeros_Success {

            @ParameterizedTest
            @ValueSource(strings = {"", "asdas", "123044", "asd102das", "    "})
            @DisplayName("removeLeadingPlusSignAndZeros with an input string that does not start with '+' or zeros should bypass the argument")
            public void removeLeadingPlusSignAndZeros_Bypass(String str) {
                Assertions.assertEquals(numberPrefixService.removeLeadingPlusSignAndZeros(str), str);
            }

            @ParameterizedTest
            @MethodSource("generateRemoveLeadingPlusSignAndZerosValidData")
            @DisplayName("removeLeadingPlusSignAndZeros with an input string that starts with '+' or zeros should return the argument without")
            public void removeLeadingPlusSignAndZeros_RemovePlusAndZeros(String input, String output) {
                Assertions.assertEquals(numberPrefixService.removeLeadingPlusSignAndZeros(input), output);
            }

            private static Stream<Arguments> generateRemoveLeadingPlusSignAndZerosValidData() {
                return Stream.of(Arguments.of("+", ""),
                                 Arguments.of("+0000", ""),
                                 Arguments.of("0", ""),
                                 Arguments.of("0000", ""),
                                 Arguments.of("+0000123", "123"),
                                 Arguments.of("+123", "123"),
                                 Arguments.of("+++123", "++123"),
                                 Arguments.of("+0000123", "123"));
            }

        }
    }

    @Nested
    @DisplayName("getPrefixFromNumber")
    class getPrefixFromNumber {

        @Nested
        @DisplayName("failure")
        class getPrefixFromNumber_Failure {

            private static final int[] BASIC_PREFIXES = {33, 34};

            @Test
            @DisplayName("getPrefixFromNumber with null prefixes array should throw IllegalArgumentException")
            public void getPrefixFromNumber_IllegalArgumentException() {
                Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
                    numberPrefixService.getPrefixFromNumber("132456", null);
                });
                Assertions.assertEquals(ERROR_NO_PREFIXES, exception.getMessage());
            }

            @Test
            @DisplayName("getPrefixFromNumber with null argument number should throw IllegalArgumentException")
            public void getPrefixFromNumber_IllegalArgumentException_Null() {
                Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
                    numberPrefixService.getPrefixFromNumber(null, BASIC_PREFIXES);
                });
                Assertions.assertEquals(ERROR_INVALID_NUMBER + " " + null, exception.getMessage());
            }

            @ParameterizedTest
            @ValueSource(strings = {"dsada", "&%$+*", "  ", ""})
            @DisplayName("getPrefixFromNumber with not numeric argument number should throw IllegalArgumentException")
            public void getPrefixFromNumber_IllegalArgumentException_NaN(String number) {
                Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
                    numberPrefixService.getPrefixFromNumber(number, BASIC_PREFIXES);
                });
                Assertions.assertEquals(ERROR_INVALID_NUMBER + " " + number, exception.getMessage());
            }

        }

        @Nested
        @DisplayName("success")
        class getPrefixFromNumber_Success {

            private static final int[] BASIC_PREFIXES = {33, 34, 93, 1, 0, 359};

            @ParameterizedTest
            @MethodSource("generateGetPrefixFromNumberValidData")
            @DisplayName("getPrefixFromNumber with a number that contains a prefix from the argument prefixes array")
            public void getPrefixFromNumber(String number, int[] prefixes, int prefix) {
                Assertions.assertEquals(numberPrefixService.getPrefixFromNumber(number, prefixes), prefix);
            }

            private static Stream<Arguments> generateGetPrefixFromNumberValidData() {
                return Stream.of(Arguments.of("3312346", BASIC_PREFIXES, 33),
                                 Arguments.of("34666111222", BASIC_PREFIXES, 34),
                                 Arguments.of("1666111222", BASIC_PREFIXES, 1),
                                 Arguments.of("0666111222", BASIC_PREFIXES, 0),
                                 Arguments.of("3591666111222", BASIC_PREFIXES, 359),
                                 Arguments.of("33777111222", BASIC_PREFIXES, 33));
            }

        }
    }
}
