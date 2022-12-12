package com.mobilemessagesgateway.service;

public interface NumberUtils {

    int getPrefixFromNumber(String number, int[] prefixes);

    String removeNumLeadingPlusSignAndZeros(String str);
}
