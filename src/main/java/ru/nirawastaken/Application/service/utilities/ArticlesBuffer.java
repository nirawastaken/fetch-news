package ru.nirawastaken.Application.service.utilities;


import ru.nirawastaken.Application.entity.Article;
import ru.nirawastaken.Application.repository.ArticleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ArticlesBuffer {


    ConcurrentHashMap<String, List<Article>> articlesHashMapByName;
    ArticleRepository articleRepository;
    List<Article> articles;
    int batchSize;

    public ArticlesBuffer(ArticleRepository articleRepository, int batchSize) {
        this.articleRepository = articleRepository;
        this.articlesHashMapByName = new ConcurrentHashMap<>();
        this.batchSize = batchSize;
    }

    public void push(Map<String, List<Article>> articlesMapByName) {
        articlesMapByName.forEach((e, s)-> {
            articlesHashMapByName.putIfAbsent(e, new ArrayList<>());
            synchronized (articlesHashMapByName.get(e)) {
                articles = articlesHashMapByName.get(e);
                articles.addAll(s);
                if(articles.size() >= batchSize) {
                    articlesHashMapByName.remove(e);
                    flush(articles);
                }
            }
        });
    }
    public void flush(List<Article> articles) {
        for (Article article: articles) {
            article.setArticle(CustomWebClient.start(
                    article.getUrl()).get().retrieve().bodyToMono(String.class).block());
        }
        articleRepository.saveAll(articles);
    }

    public void flush() {
        articlesHashMapByName.forEach((k, v) -> {
            flush(v);
        });
        flush(articles);
    }

}
