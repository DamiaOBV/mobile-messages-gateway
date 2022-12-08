package com.mobilemessagesgateway.service;

public interface NumberPrefixService {

    int getPrefixFromNumber(String number, int[] prefixes);

    String removeNumLeadingPlusSignAndZeros(String str);
}
