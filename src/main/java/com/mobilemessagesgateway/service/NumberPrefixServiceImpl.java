package com.mobilemessagesgateway.service;

import static com.mobilemessagesgateway.constants.GatewayConstants.ERROR_INVALID_NUMBER;
import static com.mobilemessagesgateway.constants.GatewayConstants.ERROR_NO_PREFIXES;
import static com.mobilemessagesgateway.constants.GatewayConstants.ERROR_PREFIX_NOT_FOUND_FOR_NUMBER;
import static com.mobilemessagesgateway.constants.GatewayConstants.ERROR_REMOVE_LEAD_PLUS_0_NULL_INPUT;

import java.util.Arrays;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@CommonsLog
public class NumberPrefixServiceImpl implements NumberPrefixService {

    @Autowired
    public NumberPrefixServiceImpl() {
    }

    /**
     * getPrefixFromNumber
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
     * removeLeadingPlusSignAndZeros
     *
     * @param number string
     * @return input string without the leading plus sign and zeros
     * @throws NullPointerException     if str is null
     * @throws IllegalArgumentException if number is not numeric or does not start with any of the provided prefixes
     */
    public String removeNumLeadingPlusSignAndZeros(String number) {
        if (number == null || !isNumeric(number)) {
            throw new IllegalArgumentException(ERROR_INVALID_NUMBER + " " + number);
        }
        if (number.startsWith("+")) {
            number = number.substring(1);
        }
        return number.replaceFirst("^0*", "");
    }

    /**
     * isNumeric
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
