package uk.gov.hmcts.reform.hmc.model.unnotifiedhearings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class PartiesNotifiedServiceData {

    private boolean hearingNoticeGenerated;
    private List<HearingDay> days;
    private String hearingLocation;
}
