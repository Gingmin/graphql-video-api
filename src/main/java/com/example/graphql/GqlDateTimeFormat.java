package com.example.graphql;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public final class GqlDateTimeFormat {
    private GqlDateTimeFormat() {}

    private static final DateTimeFormatter ISO_INSTANT =
        DateTimeFormatter.ISO_INSTANT.withZone(ZoneOffset.UTC);

    public static String formatOrNull(TemporalAccessor temporal) {
        return temporal == null ? null : ISO_INSTANT.format(temporal);
    }
}
