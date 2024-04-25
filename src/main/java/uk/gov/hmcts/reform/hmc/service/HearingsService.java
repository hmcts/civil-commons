package uk.gov.hmcts.reform.hmc.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.hmc.client.HearingsApi;
import uk.gov.hmcts.reform.hmc.exception.HmcException;
import uk.gov.hmcts.reform.hmc.model.hearing.HearingGetResponse;
import uk.gov.hmcts.reform.hmc.model.hearings.HearingsResponse;
import uk.gov.hmcts.reform.hmc.model.unnotifiedhearings.PartiesNotified;
import uk.gov.hmcts.reform.hmc.model.unnotifiedhearings.PartiesNotifiedResponses;
import uk.gov.hmcts.reform.hmc.model.unnotifiedhearings.UnNotifiedHearingResponse;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class HearingsService {

    private final HearingsApi hearingNoticeApi;
    private final AuthTokenGenerator authTokenGenerator;

    public HearingGetResponse getHearingResponse(String authToken, String hearingId) throws HmcException {
        log.debug("Sending Get Hearings with Hearing ID {}", hearingId);
        try {
            return hearingNoticeApi.getHearingRequest(
                "Bearer " + "eyJ0eXAiOiJKV1QiLCJraWQiOiJaNEJjalZnZnZ1NVpleEt6QkVFbE1TbTQzTHM9IiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJjaXZpbC1zeXN0ZW0tdXBkYXRlQGp1c3RpY2UuZ292LnVrIiwiY3RzIjoiT0FVVEgyX1NUQVRFTEVTU19HUkFOVCIsImF1dGhfbGV2ZWwiOjAsImF1ZGl0VHJhY2tpbmdJZCI6IjZmOTVkYTUwLTQwNjQtNGQ2YS1hMDE0LTBiOTgwZDBiNTE1OS01NzEzNDE2NzgiLCJzdWJuYW1lIjoiY2l2aWwtc3lzdGVtLXVwZGF0ZUBqdXN0aWNlLmdvdi51ayIsImlzcyI6Imh0dHBzOi8vZm9yZ2Vyb2NrLWFtLnNlcnZpY2UuY29yZS1jb21wdXRlLWlkYW0tZGVtby5pbnRlcm5hbDo4NDQzL29wZW5hbS9vYXV0aDIvcmVhbG1zL3Jvb3QvcmVhbG1zL2htY3RzIiwidG9rZW5OYW1lIjoiYWNjZXNzX3Rva2VuIiwidG9rZW5fdHlwZSI6IkJlYXJlciIsImF1dGhHcmFudElkIjoiWGF2d0FJekRyWmhpNzJ4MDFHZWFQZWN2QW5rIiwiYXVkIjoiaG1jdHMiLCJuYmYiOjE3MTM0NDMwNjQsImdyYW50X3R5cGUiOiJwYXNzd29yZCIsInNjb3BlIjpbIm9wZW5pZCIsInByb2ZpbGUiLCJyb2xlcyJdLCJhdXRoX3RpbWUiOjE3MTM0NDMwNjQsInJlYWxtIjoiL2htY3RzIiwiZXhwIjoxNzEzNDcxODY0LCJpYXQiOjE3MTM0NDMwNjQsImV4cGlyZXNfaW4iOjI4ODAwLCJqdGkiOiJyVml5TzNNd1V4aWJzRWE4Y0F6em5HNlQyUlEifQ.I8ms-h_uAXevnw5xKeDeD3S0kFn4jgWs3BGaATEym5xJlM7OAgv80-57T9VVY_h0Ts3sN4p-oazIC6NmcXHlggE1zXLVVNNfQG-2X20rypu3UdzX1quVdhe8S0Z6KnmS5iiI4kkhSv8r9WexYmiueieEviAuSP-R4cy7nuHz1xt30B4q6n6oBW7PLKb6GTSwVygVzEJzooio2w1j35bKRjVid_nlceGzGGmd98otROyOIBFg13k5blF8ecLUaatoFrGlMbWO6rW-OdyazMLhV1dUzs_Z8GU-YPzE8q3YpkZU3iaURSkEM4S4f958pKJLWwxUtZI8NE7gKJ4lZt6eRg",
                "Bearer " + "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjaXZpbF9zZXJ2aWNlIiwiZXhwIjoxNzEzNDU3NDYzfQ.ELDJ6ys9r8M2xvxbttyt8Tj2bWUGSsEyEtqTzZFZITEQgDYC_jdl-OUSuMvvZd1NXlezJ-pXnFFuMCJWMCdneg",
                hearingId,
                null);
        } catch (FeignException ex)  {
            log.error("Failed to retrieve hearing with Id: {} from HMC", hearingId);
            throw new HmcException(ex);
        }
    }

    public PartiesNotifiedResponses getPartiesNotifiedResponses(String authToken, String hearingId) {
        log.debug("Requesting Get Parties Notified with Hearing ID {}", hearingId);
        try {
            return hearingNoticeApi.getPartiesNotifiedRequest(
                authToken,
                authTokenGenerator.generate(),
                hearingId);
        } catch (FeignException e) {
            log.error("Failed to retrieve patries notified with Id: %s from HMC", hearingId);
            throw new HmcException(e);
        }
    }

    public ResponseEntity updatePartiesNotifiedResponse(String authToken, String hearingId,
                                                        int requestVersion, LocalDateTime receivedDateTime,
                                                        PartiesNotified payload) {
        try {
            return hearingNoticeApi.updatePartiesNotifiedRequest(
                authToken,
                authTokenGenerator.generate(),
                payload,
                hearingId,
                requestVersion,
                receivedDateTime
            );
        } catch (FeignException ex)  {
            log.error("Failed to update partiesNotified with Id: {} from HMC", hearingId);
            throw new HmcException(ex);
        }
    }

    public UnNotifiedHearingResponse getUnNotifiedHearingResponses(String authToken, String hmctsServiceCode,
                                                                   LocalDateTime hearingStartDateFrom,
                                                                   LocalDateTime hearingStartDateTo) {
        log.debug("Requesting UnNotified Hearings");
        try {
            return hearingNoticeApi.getUnNotifiedHearingRequest(
                authToken,
                authTokenGenerator.generate(),
                hmctsServiceCode,
                hearingStartDateFrom,
                hearingStartDateTo);
        } catch (FeignException e) {
            log.error("Failed to retrieve unnotified hearings");
            throw new HmcException(e);
        }
    }

    public HearingsResponse getHearings(String authToken, Long caseId, String status) {
        log.debug("Requesting Hearings for case: {}", caseId);
        try {
            return hearingNoticeApi.getHearings(
                "Bearer " + "eyJ0eXAiOiJKV1QiLCJraWQiOiJaNEJjalZnZnZ1NVpleEt6QkVFbE1TbTQzTHM9IiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJjaXZpbC1zeXN0ZW0tdXBkYXRlQGp1c3RpY2UuZ292LnVrIiwiY3RzIjoiT0FVVEgyX1NUQVRFTEVTU19HUkFOVCIsImF1dGhfbGV2ZWwiOjAsImF1ZGl0VHJhY2tpbmdJZCI6IjZmOTVkYTUwLTQwNjQtNGQ2YS1hMDE0LTBiOTgwZDBiNTE1OS01NzEzNDE2NzgiLCJzdWJuYW1lIjoiY2l2aWwtc3lzdGVtLXVwZGF0ZUBqdXN0aWNlLmdvdi51ayIsImlzcyI6Imh0dHBzOi8vZm9yZ2Vyb2NrLWFtLnNlcnZpY2UuY29yZS1jb21wdXRlLWlkYW0tZGVtby5pbnRlcm5hbDo4NDQzL29wZW5hbS9vYXV0aDIvcmVhbG1zL3Jvb3QvcmVhbG1zL2htY3RzIiwidG9rZW5OYW1lIjoiYWNjZXNzX3Rva2VuIiwidG9rZW5fdHlwZSI6IkJlYXJlciIsImF1dGhHcmFudElkIjoiWGF2d0FJekRyWmhpNzJ4MDFHZWFQZWN2QW5rIiwiYXVkIjoiaG1jdHMiLCJuYmYiOjE3MTM0NDMwNjQsImdyYW50X3R5cGUiOiJwYXNzd29yZCIsInNjb3BlIjpbIm9wZW5pZCIsInByb2ZpbGUiLCJyb2xlcyJdLCJhdXRoX3RpbWUiOjE3MTM0NDMwNjQsInJlYWxtIjoiL2htY3RzIiwiZXhwIjoxNzEzNDcxODY0LCJpYXQiOjE3MTM0NDMwNjQsImV4cGlyZXNfaW4iOjI4ODAwLCJqdGkiOiJyVml5TzNNd1V4aWJzRWE4Y0F6em5HNlQyUlEifQ.I8ms-h_uAXevnw5xKeDeD3S0kFn4jgWs3BGaATEym5xJlM7OAgv80-57T9VVY_h0Ts3sN4p-oazIC6NmcXHlggE1zXLVVNNfQG-2X20rypu3UdzX1quVdhe8S0Z6KnmS5iiI4kkhSv8r9WexYmiueieEviAuSP-R4cy7nuHz1xt30B4q6n6oBW7PLKb6GTSwVygVzEJzooio2w1j35bKRjVid_nlceGzGGmd98otROyOIBFg13k5blF8ecLUaatoFrGlMbWO6rW-OdyazMLhV1dUzs_Z8GU-YPzE8q3YpkZU3iaURSkEM4S4f958pKJLWwxUtZI8NE7gKJ4lZt6eRg",
                "Bearer " + "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjaXZpbF9zZXJ2aWNlIiwiZXhwIjoxNzEzNDU3NDYzfQ.ELDJ6ys9r8M2xvxbttyt8Tj2bWUGSsEyEtqTzZFZITEQgDYC_jdl-OUSuMvvZd1NXlezJ-pXnFFuMCJWMCdneg",
                caseId,
                status);
        } catch (FeignException e) {
            log.error("Failed to retrieve unnotified hearings");
            throw new HmcException(e);
        }
    }
}
