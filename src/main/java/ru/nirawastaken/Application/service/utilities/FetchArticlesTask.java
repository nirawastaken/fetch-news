package ru.nirawastaken.Application.service.utilities;

import org.springframework.http.MediaType;
import reactor.core.publisher.Flux;
import ru.nirawastaken.Application.entity.Article;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FetchArticlesTask implements Runnable {
    private final int start;
    private final int limit;
    private final ArticlesBuffer articlesBuffer;
    private final String articlesUrlApi;

    public FetchArticlesTask(int start, int limit, String articlesUrlApi, ArticlesBuffer articlesBuffer) {
        this.start = start;
        this.limit = limit;
        this.articlesBuffer = articlesBuffer;
        this.articlesUrlApi = articlesUrlApi;
    }

    @Override
    public void run() {
        Flux<Article> response = CustomWebClient.start(articlesUrlApi).get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("_limit", limit)
                        .queryParam("_start", start)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Article.class);

        Map<String, List<Article>> groupByNewsSite = response
                .sort()
                .toStream()
                .filter(Blacklist::check)
                .collect(Collectors.groupingBy(Article::getNewsSite));

        articlesBuffer.push(groupByNewsSite);
    }
}
