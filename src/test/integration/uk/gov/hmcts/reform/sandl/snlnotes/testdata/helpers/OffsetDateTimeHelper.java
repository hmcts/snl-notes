package uk.gov.hmcts.reform.sandl.snlnotes.testdata.helpers;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class OffsetDateTimeHelper {

    private OffsetDateTimeHelper() {
    }

    public static OffsetDateTime january2018() {
        return january(2018);
    }

    public static OffsetDateTime january(int year) {
        return OffsetDateTime.of(LocalDateTime.of(year, 1, 1, 1, 1),
            ZoneOffset.ofHoursMinutes(1, 0));
    }
}
