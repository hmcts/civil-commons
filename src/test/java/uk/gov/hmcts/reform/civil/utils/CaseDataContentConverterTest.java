package uk.gov.hmcts.reform.civil.utils;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.ccd.client.model.CaseDataContent;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.ccd.client.model.StartEventResponse;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class CaseDataContentConverterTest {
    private static final String CIVIL_CASE_ID = "12341234";
    private static final String EVENT_ID = "1234";

    @Test
    void shouldConvertToUpdatedCaseWithoutDeletingNestedData() {
        Map<String, Object> applicant1 = new HashMap<>(Map.of("firstName", "app1firstname", "lastName", "app1lastname", "email", "app1@test.com"));
        Map<String, Object> respondent1 = new HashMap<>(Map.of("firstName", "respond1firstname", "lastName", "respond1lastname", "email", "respond1@test.com"));
        Map<String, Object> originalData = new HashMap<>(Map.of("caseId", CIVIL_CASE_ID, "applicant1", applicant1, "respondent1", respondent1));
        CaseDetails caseDetails = CaseDetails.builder().data(originalData).build();
        StartEventResponse startEventResponse = StartEventResponse.builder()
            .eventId(EVENT_ID)
            .caseDetails(caseDetails)
            .build();

        Map<String, Object> updatedData = Map.of("respondent1", Map.of("firstName", "updatedfirstname", "lastName", "updatedlastname"));

        CaseDataContent converted = CaseDataContentConverter.caseDataContentFromStartEventResponse(startEventResponse, updatedData);
        Object convertData = converted.getData();
        assertThat(converted).isNotNull();

        Map<String, Object> expectedData = Map.of("caseId", CIVIL_CASE_ID, "applicant1", applicant1, "respondent1", Map.of("firstName", "updatedfirstname", "lastName", "updatedlastname", "email", "respond1@test.com"));

        assertThat(convertData.toString()).isEqualTo(new LinkedHashMap<>(expectedData).toString());
    }
}
