package com.nasr.crawlersubscriber;

import com.nasr.crawlersubscriber.service.impl.KafkaETLDataProducerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class CrawlerSubscriberApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(CrawlerSubscriberApplication.class, args);
        KafkaETLDataProducerService bean = context.getBean(KafkaETLDataProducerService.class);

    }
}
