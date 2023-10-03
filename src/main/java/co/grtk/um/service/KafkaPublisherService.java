package co.grtk.um.service;

import co.grtk.ual.dto.UserActivityLogDTO;
import co.grtk.um.config.AppConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

import static java.lang.Thread.currentThread;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaPublisherService {
    private final KafkaTemplate<String,Object> template;
    private final AppConfig appConfig;

    @Async
    public void logUserActivityAsync(UserActivityLogDTO userActivityLogDTO)  {
        log.info("Async thread virtual:" + currentThread().isVirtual());
        logUserActivity(userActivityLogDTO);
    }

    public void logUserActivity(UserActivityLogDTO userActivityLogDTO) {
        try {
            long start = System.currentTimeMillis();
            CompletableFuture<SendResult<String, Object>> future =
                    template.send(appConfig.getKafkaTopicName(), userActivityLogDTO);
            future.whenComplete((result, ex) -> {
                long elapsed = System.currentTimeMillis() - start;
                if (ex == null) {
                   log.info("Sent message=[" + userActivityLogDTO.toString() +
                            "] with offset=[" + result.getRecordMetadata().offset() + "] elapsed:" + elapsed);
                } else {
                    log.error("Unable to send message=[" +
                            userActivityLogDTO.toString() + "] elapsed:" + elapsed, ex.getMessage());
                }
            });

        } catch (Exception ex) {
            log.error("ERROR sendLogToTopic:" + appConfig.getKafkaTopicName(),  ex);
        }
    }
}
