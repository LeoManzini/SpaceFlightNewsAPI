package br.com.leomanzini.space.flight.news.service;

import br.com.leomanzini.space.flight.news.dto.ArticlesDTO;
import br.com.leomanzini.space.flight.news.dto.ResponseEntityDTO;
import br.com.leomanzini.space.flight.news.exceptions.ArticleAlreadyAtDatabaseException;
import br.com.leomanzini.space.flight.news.exceptions.ArticleNotFoundException;
import br.com.leomanzini.space.flight.news.model.Article;
import br.com.leomanzini.space.flight.news.repository.ArticleRepository;
import br.com.leomanzini.space.flight.news.utils.beans.ModelMapperMethods;
import br.com.leomanzini.space.flight.news.utils.enums.SystemMessages;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ArticleService {

    @Autowired
    private final ArticleRepository articleRepository;
    @Autowired
    private final ModelMapperMethods modelMapper;

    @Transactional(readOnly = true)
    public Page<ArticlesDTO> findAll(Pageable pageable) {
        Page<Article> articlePage = articleRepository.findAll(pageable);
        return articlePage.map(modelMapper::entityToDto);
    }

    @Transactional(readOnly = true)
    public ArticlesDTO findById(Long id) throws ArticleNotFoundException {
        return modelMapper.entityToDto(articleRepository.findById(id).orElseThrow(() -> new ArticleNotFoundException(id)));
    }

    @Transactional
    public ResponseEntityDTO createArticle(ArticlesDTO articleDTO) throws ArticleAlreadyAtDatabaseException {
        if (verifyIfArticleExists(articleDTO.getId())) {
            throw new ArticleAlreadyAtDatabaseException(articleDTO.getId());
        } else {
            articleRepository.save(modelMapper.dtoToEntity(articleDTO));
            return createResponseMessage(SystemMessages.ARTICLE_INSERTED_SUCCESS.getMessage() + articleDTO.getId());
        }
    }

    @Transactional
    public ResponseEntityDTO updateArticle(ArticlesDTO articleDTO) throws ArticleNotFoundException {
        if (verifyIfArticleExists(articleDTO.getId())) {
            articleRepository.save(modelMapper.dtoToEntity(articleDTO));
            return createResponseMessage(SystemMessages.ARTICLE_UPDATED_SUCCESS.getMessage() + articleDTO.getId());
        } else {
            throw new ArticleNotFoundException(articleDTO.getId());
        }
    }

    private boolean verifyIfArticleExists(Long id) {
        if(articleRepository.existsById(id)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    private ResponseEntityDTO createResponseMessage(String message) {
        return ResponseEntityDTO.builder().message(message).insertionDate(LocalDateTime.now()).build();
    }
}
