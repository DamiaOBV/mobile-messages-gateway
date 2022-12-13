package com.mobilemessagesgateway.service;

import static com.mobilemessagesgateway.constants.GatewayConstants.ERROR_INVALID_NUMBER;
import static com.mobilemessagesgateway.constants.GatewayConstants.ERROR_NO_PREFIXES;
import static com.mobilemessagesgateway.constants.GatewayConstants.ERROR_PREFIX_NOT_FOUND_FOR_NUMBER;

import java.util.Arrays;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@CommonsLog
public class NumberUtilsImpl implements NumberUtils {

    @Autowired
    public NumberUtilsImpl() {
    }

    /**
     * Extracts the prefix of the number (after removing leading '+' and zeros) if included in the list of prefixes.
     *
     * @param number   numeric string
     * @param prefixes array of prefixes
     * @return prefix of the number if it is provided in the prefixes
     * @throws IllegalArgumentException if number is not numeric or does not start with any of the provided prefixes, if number is null or if prefixes
     *                                  are null
     */
    public int getPrefixFromNumber(String number, int[] prefixes) {
        if (number == null || !isNumeric(number)) {
            throw new IllegalArgumentException(ERROR_INVALID_NUMBER + " " + number);
        }
        if (prefixes == null) {
            throw new IllegalArgumentException(ERROR_NO_PREFIXES);
        }
        String[] strPrefixes = Arrays.stream(prefixes).mapToObj(String::valueOf).toArray(String[]::new);

        for (int i = 0; i < strPrefixes.length; i++) {
            if (strPrefixes[i] != null && number.startsWith(strPrefixes[i])) {
                return prefixes[i];
            }
        }

        throw new IllegalArgumentException(ERROR_PREFIX_NOT_FOUND_FOR_NUMBER + " " + number + " " + Arrays.toString(prefixes));
    }

    /**
     * Removes the first '+' sign of a number and/or all of the leading zeros. If the string without the '+' sign is not a number it will throw an
     * error.
     *
     * @param number string
     * @return input string without the leading plus sign and zeros
     * @throws IllegalArgumentException if number is null, is not numeric or does not start with any of the provided prefixes
     */
    public String removeNumLeadingPlusSignAndZeros(String number) {
        if (number == null) {
            throw new IllegalArgumentException(ERROR_INVALID_NUMBER + " " + number);
        }

        number = number.replaceFirst("^\\+", "");

        if (!isNumeric(number)) {
            throw new IllegalArgumentException(ERROR_INVALID_NUMBER + " " + number);
        }
        return number.replaceFirst("^0*", "");
    }

    /**
     * Validates if a string can be parsed to a long.
     *
     * @param number string
     * @return true if the string can be parsed to an integer and false if not
     */
    private boolean isNumeric(String number) {
        try {
            Long.parseLong(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
