package uk.gov.hmcts.reform.civil.referencedata.exception;

public class ReferenceDataLookupException extends RuntimeException {

    public static final String MESSAGE_TEMPLATE = "Failed to Data Lookup from ";

    public ReferenceDataLookupException(Throwable t) {
        super(MESSAGE_TEMPLATE, t);
    }
}
