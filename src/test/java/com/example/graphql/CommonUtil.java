package com.example.graphql;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class CommonUtil {
    public static final DateTimeFormatter ISO_INSTANT = DateTimeFormatter.ISO_INSTANT.withZone(ZoneOffset.UTC);
}
