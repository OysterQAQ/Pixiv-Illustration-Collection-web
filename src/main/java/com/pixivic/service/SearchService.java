package com.pixivic.service;

import com.pixivic.model.PixivResponse;
import com.pixivic.model.RelatedTag;
import com.pixivic.util.SearchUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SearchService {
    private final SearchUtil searchUtil;

    public Mono<PixivResponse> search(String keyword, Integer page, String startDate, String endDate) throws UnsupportedEncodingException {
        return searchUtil.search(keyword, page, startDate, endDate);
    }

    public Mono<PixivResponse> search(String keyword, Integer page) throws UnsupportedEncodingException {
        return searchUtil.search(keyword, page);
    }

    public Mono<String[]> getCandidateWords(String keyword) {
        return searchUtil.getCandidateWords(keyword).map(candidate -> candidate.getSearch_auto_complete_keywords());
    }

    public Mono<RelatedTag[]> getRelatedTags(String keyword) throws UnsupportedEncodingException {
        return searchUtil.getRelatedTags(keyword);
    }
}
