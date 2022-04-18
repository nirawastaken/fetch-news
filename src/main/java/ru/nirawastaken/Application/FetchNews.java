package ru.nirawastaken.Application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.nirawastaken.Application.repository.ArticleRepository;
import ru.nirawastaken.Application.service.utilities.ArticlesBuffer;
import ru.nirawastaken.Application.service.utilities.FetchArticlesTask;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class FetchNews {
    @Autowired
    ArticleRepository articleRepository;
    @Value("${threads-count}")
    int threadsCount;
    @Value("${max-articles-for-download}")
    int maxArticlesForDownload;
    @Value("${max-articles-for-one-thread}")
    int maxArticlesForOneThread;
    @Value("${articles-url-api}")
    String baseUrl;
    @Value("${articles-timeout-api}")
    Duration timeout;

    @Scheduled(fixedRate = Long.MAX_VALUE)
    public void start() {
        long startTime = System.currentTimeMillis();

        ArticlesBuffer articlesBuffer = new ArticlesBuffer(articleRepository);
        ExecutorService executorService = Executors.newFixedThreadPool(threadsCount);

        int threadCount = (maxArticlesForDownload - 1 + maxArticlesForOneThread) / maxArticlesForOneThread;
        int offset, start;
        for (int i = 0; i < threadCount; i++) {
            start = i * maxArticlesForOneThread;
            offset = Math.min(maxArticlesForDownload - start , maxArticlesForOneThread);
            executorService.submit(new FetchArticlesTask(start, offset, baseUrl, articlesBuffer));
        }

        executorService.shutdown();
        try {
            if (executorService.awaitTermination(timeout.getSeconds(), TimeUnit.SECONDS)) {
                log.info("Complete termination executor pool");
            }
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
        finally {
            articlesBuffer.flush();
        }
        log.info("Processing time - {} seconds", (System.currentTimeMillis() - startTime)/1000);
    }
}
