package com.sample.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.DeleteMessageResult;

@Component
public class MessageAdapter {

  private static final Logger logger = LoggerFactory.getLogger(MessageAdapter.class);

  @Autowired
  private AmazonSQS amazonSQS;

  @Value("${MESSAGING_QUEUE:}")
  private String messagingQueue;

  @SqsListener(value = "${MESSAGING_QUEUE}", deletionPolicy = SqsMessageDeletionPolicy.NEVER)
  public void receiveMessages(@Payload String payload, @Headers Map<String, Object> headers)
      throws InterruptedException, ExecutionException {
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    String message = String.format(dateFormat.format(new Date())
        + " >> Received message from SQS...Payload: %s\n , Headers %s\n ", payload, headers);
    logger.debug(message);
    System.out.println(message);
    // Thread.sleep(2000);
    deleteMessage(headers);
  }

  private void deleteMessage(Map<String, Object> headers)
      throws InterruptedException, ExecutionException {
    DeleteMessageResult result =
        amazonSQS.deleteMessage(messagingQueue, headers.get("ReceiptHandle").toString());
    System.out.println("Delete message " + result);
  }
}
