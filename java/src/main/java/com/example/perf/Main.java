package com.example.perf;

import fi.iki.elonen.NanoHTTPD;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicLong;
import java.util.TimeZone;

public class Main extends NanoHTTPD {
    private static final SimpleDateFormat RFC3339 =
        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    private static final TimeZone UTC = TimeZone.getTimeZone("UTC");
    private static final Calendar CALENDAR = Calendar.getInstance(UTC);

    private final AtomicLong counter = new AtomicLong();

    static {
        RFC3339.setTimeZone(UTC);
    }

    public Main() throws IOException {
        super(8080);
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
    }

    public static void main(String[] args) {
        try {
            new Main();
        } catch (IOException exc) {
            throw new RuntimeException("Main failure", exc);
        }
    }

    @Override
    public Response serve(IHTTPSession session) {
        return newFixedLengthResponse(String.format(
                    "Speed Test Response\n"+
                    "This is request #%06d\n"+
                    "Current time is %s",
                    counter.addAndGet(1),
                    RFC3339.format(CALENDAR.getTime())));
    }
}
