package com.kulsin.wallet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BeanViewer {

    @EventListener
    public void showBeansRegistered(ApplicationReadyEvent event) {
        String[] beanNames = event.getApplicationContext().getBeanDefinitionNames();
        for (String beanName : beanNames) {
            log.info("BeanName=[{}]", beanName);
        }
    }
}