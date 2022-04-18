package ru.nirawastaken.Application.service;

import org.springframework.stereotype.Service;
import ru.nirawastaken.Application.entity.Article;

import java.util.List;
import java.util.Optional;

@Service
public interface ArticleService {
    public List<Article> getAllArticles();
    public Optional<Article> getArticleById(Integer id);
    public List<Article> getArticlesByNewsSite(String newsSite);
}
