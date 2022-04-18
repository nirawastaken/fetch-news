package ru.nirawastaken.Application.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nirawastaken.Application.entity.Article;
import ru.nirawastaken.Application.repository.ArticleRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    ArticleRepository repository;

    @Override
    public List<Article> getAllArticles() {return repository.findAll();}

    @Override
    public Optional<Article> getArticleById(Integer id) {return repository.findById(id);}

    @Override
    public List<Article> getArticlesByNewsSite(String newsSite) {
        return repository.findByNewsSite(newsSite);
    }
}