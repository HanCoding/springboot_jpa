package com.sh.jpaboard.service;

import com.sh.jpaboard.entity.Article;
import com.sh.jpaboard.repository.ArticleRepository;
import com.sh.jpaboard.vo.ArticleVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {

    private ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public void insert(ArticleVo articleVo) {

        articleVo.setRegDate(LocalDateTime.now());
        articleVo.setUpdateDate(LocalDateTime.now());

        articleRepository.save(articleVo.toEntity());
    }

    public ArticleVo articleView(Long id) {

        Optional<Article> optArticle = articleRepository.findById(id);

        Article article = optArticle.get();

        ArticleVo articleVo = ArticleVo.builder()
                .id(article.getId())
                .writer(article.getWriter())
                .regDate(article.getRegDate())
                .title(article.getTitle())
                .content(article.getContent())
                .build();

        return articleVo;
    }

    public Page<Article> articleList(Pageable pageable) {
        Page<Article> articles = articleRepository.findAll(pageable);
        return articles;
    }

    public Page<Article> articleSearchList(String title, Pageable pageable) {
        Page<Article> articles = articleRepository
                .findByTitleContainingOrContentContainingOrWriterContaining(title, title, title, pageable);

        return articles;
    }

    public void articleDelete(long id) {

        Optional<Article> article = articleRepository.findById(id);

        article.ifPresent(selected -> {
            articleRepository.delete(selected);

        });

    }
}
