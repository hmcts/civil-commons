package uk.gov.hmcts.reform.civil.bankholidays;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.civil.helpers.ResourceReader;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.function.Predicate;

@Slf4j
@Service
public class NonWorkingDaysCollection {

    private final String dataResource;

    public NonWorkingDaysCollection(@Value("${nonworking-days.datafile}") String dataSource) {
        this.dataResource = dataSource;
    }

    public boolean contains(LocalDate date) {
        final String isoDate = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        try {
            String data = ResourceReader.readString(dataResource);
            return Arrays.stream(data.split("[\r\n]+"))
                .map(String::trim)
                .anyMatch(Predicate.isEqual(isoDate));
        } catch (IllegalStateException e) {
            // thrown from ResourceReader#readString
            return false;
        }
    }
}
