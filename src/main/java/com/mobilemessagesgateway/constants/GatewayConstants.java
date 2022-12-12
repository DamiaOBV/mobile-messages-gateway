package com.mobilemessagesgateway.constants;

public class GatewayConstants {

    //Errors
    public static final String ERROR_INVALID_NUMBER = "Invalid number";
    public static final String ERROR_PREFIX_NOT_FOUND_FOR_NUMBER = "Number does not start with any of the supported prefixes";
    public static final String ERROR_REMOVE_LEAD_PLUS_0_NULL_INPUT = "removeLeadingPlusSignAndZeros input string can not be null";
    public static final String ERROR_NO_PREFIXES = "There are no prefixes";
    public static final String ERROR_NO_PROVIDERS = "There are no providers";

    //Status
    public static final String STATUS_RECEIVED = "RECEIVED";
    public static final String STATUS_SENT = "SENT";
    public static final String STATUS_ERROR = "ERROR";

    //Literals
    public static final String SOAP_LITERAL = "SOAP";
    public static final String REST_LITERAL = "REST";
    public static final String RMI_LITERAL = "RMI";
}
