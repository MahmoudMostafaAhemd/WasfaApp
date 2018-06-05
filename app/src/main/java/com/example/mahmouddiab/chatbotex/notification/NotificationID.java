package com.example.mahmouddiab.chatbotex.notification;


import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by mahmoud on 13-Jan-17.
 */

public class NotificationID {
    private final static AtomicInteger c = new AtomicInteger(0);

    public static int getID() {
        return c.incrementAndGet();
    }
}

