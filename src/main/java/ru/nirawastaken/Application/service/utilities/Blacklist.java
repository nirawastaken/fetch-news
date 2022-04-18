package ru.nirawastaken.Application.service.utilities;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.nirawastaken.Application.entity.Article;

import java.util.List;

@Service
public class Blacklist implements InitializingBean {

    @Value("${black-list-string}")
    List<String> blacklist;
    private static Blacklist instance;

    public static boolean check(Article article) {
        if(instance!=null) {
            String title = article.getTitle().toLowerCase();
            for (String words : instance.blacklist) {
                if (title.contains(words.toLowerCase())) return false;
            }
        }
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        instance = this;
    }
}
