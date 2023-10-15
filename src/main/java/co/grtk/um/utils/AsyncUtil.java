package co.grtk.um.utils;


import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class AsyncUtil {


    /**
     * If any future from the list throws an exception, fail the end future and cancel all futures in the list
     *
     * @param futures The futures to bundle
     */
    public static void joinFutures(List<CompletableFuture<?>> futures, ThreadPoolTaskExecutor taskExecutor) {
        try {
            long start = System.currentTimeMillis();
            log.info("TstTaskExecutor before await completion futures:{} active:{} queueSize:{} poolSize:{} corePoolSize:{} maxPoolSize:{} queueCapacity:{}",
                    futures.size(),
                    taskExecutor.getActiveCount(),
                    taskExecutor.getQueueSize(),
                    taskExecutor.getPoolSize(),
                    taskExecutor.getCorePoolSize(),
                    taskExecutor.getMaxPoolSize(),
                    taskExecutor.getQueueCapacity());
            CompletableFuture<Void> allWithFailFast = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

            futures.forEach(completableFuture -> completableFuture.exceptionally(e -> {
                allWithFailFast.completeExceptionally(e);
                futures.forEach(future -> future.cancel(true));
                return null;
            }));
            allWithFailFast.join();

            long elapsed = System.currentTimeMillis() - start;
            log.info("TstTaskExecutor after completion futures:{} active:{} core:{} elapsed:{}",
                    futures.size(),
                    taskExecutor.getActiveCount(),
                    taskExecutor.getCorePoolSize(),
                    elapsed);

        } catch (CompletionException e) {
            handleException(e);
        }
    }

    private static void handleException(CompletionException e) {
        throw new RuntimeException(e);
    }
}