package com.sample.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class MessageAdapter {

  private static final Logger logger = LoggerFactory.getLogger(MessageAdapter.class);

  @SqsListener(value = "${MESSAGING_QUEUE}", deletionPolicy = SqsMessageDeletionPolicy.NEVER)
  public void receiveMessages(@Payload String payload, @Headers Map<String, Object> headers) {
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    Date date = new Date();
    String message = String.format(
        dateFormat.format(date) + " >> Received message from SQS...Payload: %s\n , Headers %s\n ",
        payload, headers);
    logger.debug(message);
    System.out.println(message);
  }

}
