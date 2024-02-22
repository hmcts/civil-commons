package uk.gov.hmcts.reform.hmc.exception;

public class HmcException extends RuntimeException {

    public static final String MESSAGE_TEMPLATE = "Failed to retrieve data from HMC due to: %s";

    public HmcException(Throwable t) {
        super(String.format(MESSAGE_TEMPLATE, t));
    }
}
