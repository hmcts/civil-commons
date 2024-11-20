package uk.gov.hmcts.reform.civil.referencedata.exception;

public class ReferenceDataLookupException extends RuntimeException {

    public static final String MESSAGE_TEMPLATE = "Location Reference Data Lookup Failed";

    public ReferenceDataLookupException(Throwable t) {
        super(MESSAGE_TEMPLATE, t);
    }
}
