package ru.nirawastaken.Application.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.nirawastaken.Application.entity.Article;
import ru.nirawastaken.Application.service.ArticleService;

import java.util.List;
import java.util.Optional;

@RestController
public class ArticleController {

    @Autowired
    ArticleService articleService;

    @GetMapping(value = "/all")
    public List<Article> getAllArticles(){
        return articleService.getAllArticles();
    }
    @GetMapping(value = "/id={id}")
    public Optional<Article> getArticleById(@PathVariable("id") Integer id){
        return articleService.getArticleById(id);
    }
    @GetMapping(value = "/site={newsSite}")
    public List<Article> getArticlesByNewsSite(@PathVariable("newsSite") String newsSite){
        return articleService.getArticlesByNewsSite(newsSite);
    }
}
