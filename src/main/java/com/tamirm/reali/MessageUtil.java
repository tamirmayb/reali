package com.tamirm.reali;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class MessageUtil {
    private final static int PATTERN_TIME_SIZE = 17;

    public static MessagesAppServer.Message formatMessage(String input) {
        String messageText;
        long messageTime;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        try {
            messageText = input.substring(0, input.length() - PATTERN_TIME_SIZE).trim();
            messageTime = LocalDateTime.parse(input.substring(input.length() - PATTERN_TIME_SIZE).trim(), formatter)
                    .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        } catch (Exception e) {
            messageText = input;
            messageTime = LocalDateTime.now().plusSeconds(20).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }
        return new MessagesAppServer.Message(messageText + "_" + messageTime, messageTime);
    }

}
