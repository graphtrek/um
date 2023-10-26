package co.grtk.um.service;

import co.grtk.um.dto.BeanInitDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
public class BootLoggerService implements BeanPostProcessor, Ordered {

    private final Map<String, Long> start;
    private final Map<String, Long> end;
    public BootLoggerService() {
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

    public List<BeanInitDTO> logAllBeansInitializationTime() {
        List<BeanInitDTO> beanInitDTOS = new ArrayList<>();
        AtomicLong sum = new AtomicLong();


        AtomicLong gapSum = new AtomicLong(0);
        AtomicLong previous = new AtomicLong(0);

        end.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))

                .forEach(entry -> {
                    long gap = previous.get() - entry.getValue();
                    if(gap > 0)
                        gapSum.addAndGet(gap);
                    else
                        gap = 0;
                    long initTime = initializationTime(entry.getKey());
                    sum.addAndGet(initTime);
                    previous.set(entry.getValue());
                    LocalDateTime startTime =
                            Instant.ofEpochMilli(start.get(entry.getKey())).atZone(ZoneId.systemDefault()).toLocalDateTime();
                    LocalDateTime endTime =
                            Instant.ofEpochMilli(entry.getValue()).atZone(ZoneId.systemDefault()).toLocalDateTime();
                    beanInitDTOS.add(new BeanInitDTO(entry.getKey(),initTime,startTime, endTime, gap));

                });
        log.info("Application Ready Event Beans initialization took:{} ms gaps between beans:{} ms", sum.get(), gapSum.get());
        return beanInitDTOS;
    }


    @EventListener(ApplicationReadyEvent.class)
    public void eventListenerExecute(ApplicationReadyEvent event) {
        log.info("Application Ready Event is successfully Started");
        BootLoggerService bootLoggerService = event.getApplicationContext().getBean(BootLoggerService.class);
        bootLoggerService.
                logAllBeansInitializationTime().
                stream().
                filter(beanInitDTO -> beanInitDTO.elapsed() > 100).
                forEach(beanInitDTO -> log.info("Bean {} initialization took {} ms  gap:{}", beanInitDTO.beanName(),  beanInitDTO.elapsed(), beanInitDTO.gap()));
    }
}