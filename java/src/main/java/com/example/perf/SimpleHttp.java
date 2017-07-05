package com.example.perf;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.Calendar;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;

public class SimpleHttp {

    static final String headerTemplate =
        "HTTP/1.1 200 OK\r\n" +
        "Date: %s\r\n" +
        "Server: Java NIO\r\n"+
        "Connection: close\r\n"+
        "Content-Length: %d\r\n" +
        "\r\n";
    static final String contentTemplate = "Speed Test Response\n"+
            "This is request #%06d\n"+
            "Current time is %s";

    static final SimpleDateFormat RFC3339 =
//            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
    static final TimeZone UTC = TimeZone.getTimeZone("UTC");
    static final Calendar CALENDAR = Calendar.getInstance(UTC);
    static final Charset charset = Charset.forName("UTF-8");
    static final CharsetEncoder encoder = charset.newEncoder();
    static final AtomicLong counter = new AtomicLong();

    public static void main(String[] args) throws Exception {
        System.out.println("Start");
        ServerSocketChannel server = ServerSocketChannel.open();
        Selector selector = Selector.open();
        server.socket().bind(new InetSocketAddress(8080));
        server.configureBlocking(false);
        server.register(selector, SelectionKey.OP_ACCEPT);
        try {
            while (true) {
                selector.select();
                Iterator<SelectionKey> i = selector.selectedKeys().iterator();
                while (i.hasNext()) {
                    SelectionKey key = i.next();
                    i.remove();
                    if (!key.isValid()) {
                        continue;
                    }
                    if (key.isAcceptable()) {
                        SocketChannel client = server.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ);
                    } else if (key.isReadable()) {
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(256);
                        client.read(buffer);

                        String content = String.format(contentTemplate,
                                counter.addAndGet(1),
                                RFC3339.format(CALENDAR.getTime()));
                        String header = String.format(headerTemplate,
                                RFC3339.format(CALENDAR.getTime()),
                                content.length());

                        client.write(encoder.encode(CharBuffer.wrap(header)));
                        client.write(ByteBuffer.wrap(content.getBytes()));

                        client.close();
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println(counter.get());
            ex.printStackTrace();
        }
    }
}