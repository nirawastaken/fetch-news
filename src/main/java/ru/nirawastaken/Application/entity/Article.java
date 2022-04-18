package ru.nirawastaken.Application.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ARTICLES")
public class Article implements Comparable<Article> {

    @Id
    @Column(name = "id")
    int id;
    @Column(name = "title")
    String title;
    @Transient
    String url;
    @Column(name = "news_site")
    String newsSite;
    @Column(name = "published_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    Date publishedAt;
    @Column(name = "article", columnDefinition="TEXT")
    String article;

    @Override
    public int compareTo(Article o) {
        if(this.publishedAt.equals(o.publishedAt)) return 0;
        else if (this.publishedAt.before(o.publishedAt)) return 1;
        else return -1;
    }
}
