package com.sample.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.aws.core.region.RegionProvider;
import org.springframework.cloud.aws.core.region.StaticRegionProvider;
import org.springframework.cloud.aws.messaging.config.SimpleMessageListenerContainerFactory;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSqs;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.services.sqs.AmazonSQSAsync;

@Configuration
@EnableSqs
@ConditionalOnProperty(name = {"MESSAGING_QUEUE", "AWS_ACCESS_KEY", "AWS_SECRET_KEY"})
public class SQSConfiguration {

  @Value("${MESSAGING_QUEUE_REGION:}")
  private String region;

  @Bean
  @ConditionalOnProperty(name = {"MESSAGING_QUEUE_REGION"})
  public RegionProvider regionProvider() {
    return new StaticRegionProvider(region);
  }

  @Bean
  public SimpleMessageListenerContainerFactory simpleMessageListenerContainerFactory(
      AmazonSQSAsync amazonSqs) {
    SimpleMessageListenerContainerFactory factory = new SimpleMessageListenerContainerFactory();
    factory.setAmazonSqs(amazonSqs);
    factory.setAutoStartup(true);
    factory.setMaxNumberOfMessages(10);
    factory.setWaitTimeOut(10);
    factory.setVisibilityTimeout(2 * 60);// 2 minutes
    return factory;
  }

}
