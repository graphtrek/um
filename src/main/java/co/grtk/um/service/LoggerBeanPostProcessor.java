package co.grtk.um.service;

import co.grtk.um.dto.BeanInitDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@Component
public class LoggerBeanPostProcessor implements BeanPostProcessor, Ordered {

    private final Map<String, Long> start;
    private final Map<String, Long> end;
    public LoggerBeanPostProcessor() {
        start = new HashMap<>();
        end = new HashMap<>();
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        start.put(beanName, System.currentTimeMillis());
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        end.put(beanName, System.currentTimeMillis());
        log.debug("Init time for " + beanName + ": " + initializationTime(beanName));
        return bean;
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

    public long initializationTime(String beanName) {
        return end.get(beanName) - start.get(beanName);
    }

    public List<BeanInitDTO> logAllBeansInitializationTime(int maxTimeInMillis) {
        List<BeanInitDTO> beanInitDTOS = new ArrayList<>();
        Map<String, Long> map = new HashMap<>();
        end.forEach((k,v) -> map.put(k, initializationTime(k)));

        map.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .filter(entry -> entry.getValue() > maxTimeInMillis)
                .forEach(entry -> {
                    LocalDateTime startTime =
                            Instant.ofEpochMilli(start.get(entry.getKey())).atZone(ZoneId.systemDefault()).toLocalDateTime();
                    LocalDateTime endTime =
                            Instant.ofEpochMilli(end.get(entry.getKey())).atZone(ZoneId.systemDefault()).toLocalDateTime();
                    beanInitDTOS.add(new BeanInitDTO(entry.getKey(),initializationTime(entry.getKey()),startTime, endTime));

                });
        return beanInitDTOS;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void eventListenerExecute(ApplicationReadyEvent event) {
        log.info("Application Ready Event is successfully Started");
        LoggerBeanPostProcessor loggerBeanPostProcessor = event.getApplicationContext().getBean(LoggerBeanPostProcessor.class);
        loggerBeanPostProcessor.logAllBeansInitializationTime(100).
                forEach(beanInitDTO -> log.info("Bean {} initialization took {} ms ", beanInitDTO.beanName(),  beanInitDTO.elapsed()));
    }
}