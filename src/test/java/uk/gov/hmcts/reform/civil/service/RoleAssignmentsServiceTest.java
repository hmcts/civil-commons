package uk.gov.hmcts.reform.civil.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.civil.ras.client.RoleAssignmentsApi;
import uk.gov.hmcts.reform.civil.ras.model.GrantType;
import uk.gov.hmcts.reform.civil.ras.model.RoleAssignment;
import uk.gov.hmcts.reform.civil.ras.model.RoleAssignmentRequest;
import uk.gov.hmcts.reform.civil.ras.model.RoleAssignmentResponse;
import uk.gov.hmcts.reform.civil.ras.model.RoleAssignmentServiceResponse;
import uk.gov.hmcts.reform.civil.ras.model.RoleCategory;
import uk.gov.hmcts.reform.civil.ras.model.RoleRequest;
import uk.gov.hmcts.reform.civil.ras.model.RoleType;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {
    RoleAssignmentsService.class
})
class RoleAssignmentsServiceTest {

    private static final String USER_AUTH_TOKEN = "Bearer caa-user-xyz";
    private static final String SERVICE_TOKEN = "Bearer service-token";
    private static final String ACTORID = "1111111";
    private static RoleAssignmentServiceResponse RAS_RESPONSE = RoleAssignmentServiceResponse
        .builder()
        .roleAssignmentResponse(
            List.of(RoleAssignmentResponse
                        .builder()
                        .actorId(ACTORID)
                        .build()
            )
        )
        .build();

    @MockBean
    private AuthTokenGenerator authTokenGenerator;

    @MockBean
    private RoleAssignmentsApi roleAssignmentApi;

    @Autowired
    private RoleAssignmentsService roleAssignmentsService;

    @BeforeEach
    void init() {
        clearInvocations(authTokenGenerator);
        when(authTokenGenerator.generate()).thenReturn(SERVICE_TOKEN);
        when(roleAssignmentApi.getRoleAssignments(anyString(), anyString(), anyString())).thenReturn(RAS_RESPONSE);
    }

    @Test
    void shouldReturn() {
        var roleAssignmentsExpected = roleAssignmentsService.getRoleAssignments(ACTORID, USER_AUTH_TOKEN);
        assertEquals(roleAssignmentsExpected, RAS_RESPONSE);
    }

    @Test
    void shouldPostExpectedPayload() {
        RoleAssignmentRequest request = RoleAssignmentRequest.builder()
            .roleRequest(RoleRequest.builder()
                             .assignerId(ACTORID)
                             .reference("civil-hearings-system-user")
                             .process("civil-system-user")
                             .replaceExisting(true)
                             .build())
            .requestedRoles(List.of(
                RoleAssignment.builder()
                    .actorId(ACTORID)
                    .actorIdType("IDAM")
                    .roleType(RoleType.ORGANISATION)
                    .classification("PUBLIC")
                    .grantType(GrantType.STANDARD)
                    .roleCategory(RoleCategory.SYSTEM)
                    .roleName("some-role")
                    .attributes(Map.of("jurisdiction", "CIVIL", "caseType", "CIVIL"))
                    .readOnly(false)
                    .build()
            )).build();

        roleAssignmentsService.assignUserRoles(ACTORID, USER_AUTH_TOKEN, request);

        verify(roleAssignmentApi, times(1))
            .createRoleAssignment(eq(USER_AUTH_TOKEN), eq(SERVICE_TOKEN), eq(request));
    }

}
