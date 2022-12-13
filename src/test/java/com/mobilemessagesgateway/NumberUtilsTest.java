package com.mobilemessagesgateway;

import static com.mobilemessagesgateway.constants.GatewayConstants.ERROR_INVALID_NUMBER;
import static com.mobilemessagesgateway.constants.GatewayConstants.ERROR_NO_PREFIXES;
import static com.mobilemessagesgateway.constants.GatewayConstants.ERROR_PREFIX_NOT_FOUND_FOR_NUMBER;

import com.mobilemessagesgateway.service.NumberUtils;
import java.util.Arrays;
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
@DisplayName("NumberUtils")
public class NumberUtilsTest {

    @Autowired
    NumberUtils numberUtils;

    private static final int[] PREFIXES = {33, 34, 93, 1, 0, 359};

    @Nested
    @DisplayName("removeNumLeadingPlusSignAndZeros")
    class removeNumLeadingPlusSignAndZeros {

        @Nested
        @DisplayName("failure")
        class removeNumLeadingPlusSignAndZeros_Failure {

            @Test
            @DisplayName("removeNumLeadingPlusSignAndZeros with null input should throw IllegalArgumentException")
            public void removeNumLeadingPlusSignAndZeros_IllegalArgumentException() {
                Exception exception = Assertions.assertThrows(IllegalArgumentException.class,
                                                              () -> numberUtils.removeNumLeadingPlusSignAndZeros(null));
                Assertions.assertEquals(ERROR_INVALID_NUMBER + " " + null, exception.getMessage());
            }

            @ParameterizedTest
            @MethodSource("generateremoveNumLeadingPlusSignAndZerosInvalidData")
            @DisplayName("removeNumLeadingPlusSignAndZeros with non numeric input should throw IllegalArgumentException")
            public void removeNumLeadingPlusSignAndZeros_RemovePlusAndZeros(String number, String prettyNumber) {
                Exception exception = Assertions.assertThrows(IllegalArgumentException.class,
                                                              () -> numberUtils.removeNumLeadingPlusSignAndZeros(number));
                Assertions.assertEquals((ERROR_INVALID_NUMBER + " " + prettyNumber), exception.getMessage());
            }

            private static Stream<Arguments> generateremoveNumLeadingPlusSignAndZerosInvalidData() {
                return Stream.of(Arguments.of("asdas", "asdas"),
                                 Arguments.of("+asd102das", "asd102das"),
                                 Arguments.of("", ""),
                                 Arguments.of("    ", "    "),
                                 Arguments.of("+", ""),
                                 Arguments.of("+++123", "++123"));
            }
        }

        @Nested
        @DisplayName("success")
        class removeNumLeadingPlusSignAndZeros_Success {

            @ParameterizedTest
            @MethodSource("generateremoveNumLeadingPlusSignAndZerosValidData")
            @DisplayName(
                    "removeNumLeadingPlusSignAndZeros with a number that starts with '+' or zeros should return the number without the zeros or/and the '+'")
            public void removeNumLeadingPlusSignAndZeros_RemovePlusAndZeros(String input, String output) {
                Assertions.assertEquals(numberUtils.removeNumLeadingPlusSignAndZeros(input), output);
            }

            private static Stream<Arguments> generateremoveNumLeadingPlusSignAndZerosValidData() {
                return Stream.of(Arguments.of("+0000", ""),
                                 Arguments.of("0", ""),
                                 Arguments.of("0000", ""),
                                 Arguments.of("+0000123", "123"),
                                 Arguments.of("+123", "123"),
                                 Arguments.of("15616123", "15616123"),
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

            @Test
            @DisplayName("getPrefixFromNumber with null prefixes array should throw IllegalArgumentException")
            public void getPrefixFromNumber_IllegalArgumentException() {
                Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> numberUtils.getPrefixFromNumber("132456", null));
                Assertions.assertEquals(ERROR_NO_PREFIXES, exception.getMessage());
            }

            @Test
            @DisplayName("getPrefixFromNumber with non existing prefix should throw IllegalArgumentException")
            public void getPrefixFromNumber_NonExistingPrefix() {
                String noPrefixNum = "+39122456";
                Exception exception = Assertions.assertThrows(IllegalArgumentException.class,
                                                              () -> numberUtils.getPrefixFromNumber(noPrefixNum, PREFIXES));
                Assertions.assertEquals((ERROR_PREFIX_NOT_FOUND_FOR_NUMBER + " " + noPrefixNum + " " + Arrays.toString(PREFIXES)),
                                        exception.getMessage());
            }

            @Test
            @DisplayName("getPrefixFromNumber with null argument number should throw IllegalArgumentException")
            public void getPrefixFromNumber_IllegalArgumentException_Null() {
                Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> numberUtils.getPrefixFromNumber(null, PREFIXES));
                Assertions.assertEquals(ERROR_INVALID_NUMBER + " " + null, exception.getMessage());
            }

            @ParameterizedTest
            @ValueSource(strings = {"dsada", "&%$+*", "  ", ""})
            @DisplayName("getPrefixFromNumber with not numeric argument number should throw IllegalArgumentException")
            public void getPrefixFromNumber_IllegalArgumentException_NaN(String number) {
                Exception exception = Assertions.assertThrows(IllegalArgumentException.class,
                                                              () -> numberUtils.getPrefixFromNumber(number, PREFIXES));
                Assertions.assertEquals(ERROR_INVALID_NUMBER + " " + number, exception.getMessage());
            }

        }

        @Nested
        @DisplayName("success")
        class getPrefixFromNumber_Success {

            @ParameterizedTest
            @MethodSource("generateGetPrefixFromNumberValidData")
            @DisplayName("getPrefixFromNumber with an input that contains a prefix from the argument prefixes array should return the number prefix")
            public void getPrefixFromNumber(String prettyNumber, int[] prefixes, int prefix) {
                Assertions.assertEquals(numberUtils.getPrefixFromNumber(prettyNumber, prefixes), prefix);
            }

            private static Stream<Arguments> generateGetPrefixFromNumberValidData() {
                return Stream.of(Arguments.of("3312346", PREFIXES, 33),
                                 Arguments.of("34666111222", PREFIXES, 34),
                                 Arguments.of("1666111222", PREFIXES, 1),
                                 Arguments.of("0666111222", PREFIXES, 0),
                                 Arguments.of("3591666111222", PREFIXES, 359),
                                 Arguments.of("33777111222", PREFIXES, 33));
            }

        }
    }
}
