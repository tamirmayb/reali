package com.tamirm.reali;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class MessageUtil {
    private final static String MESSAGE_TEXT_PARAM_NAME = "text";
    private final static String MESSAGE_ECHO_DATE_PARAM_NAME = "echo";
    private final static String MESSAGE_ECHO_DATE_PATTERN = "yyyy-MM-dd HH:mm";

    public static MessagesAppServer.Message formatMessage(String input) {
        String messageText = "";
        long messageTime = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(MESSAGE_ECHO_DATE_PATTERN);

        try {
            String[] params = input.split("&");
            for(String param : params) {
                String[] paramKeyAndValue = param.split("=");
                if(paramKeyAndValue[0].trim().equals(MESSAGE_TEXT_PARAM_NAME)) {
                    messageText = paramKeyAndValue[1];
                } else if(paramKeyAndValue[0].trim().equals(MESSAGE_ECHO_DATE_PARAM_NAME)) {
                    messageTime = LocalDateTime.parse(paramKeyAndValue[1].trim(), formatter)
                            .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                }
            }
            if(messageText.isEmpty()) {
                throw new IllegalArgumentException("Cannot echo message due to invalid parameters ");
            } else {
                if(messageTime == 0) {
                    messageTime = LocalDateTime.now().plusSeconds(20).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot echo message due to invalid parameters " + e.getMessage());
        }
        return new MessagesAppServer.Message(messageText + "_" + messageTime, messageTime);
    }

}
