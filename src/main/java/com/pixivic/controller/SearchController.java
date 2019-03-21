package com.pixivic.controller;

import com.pixivic.model.Illustration;
import com.pixivic.model.RelatedTag;
import com.pixivic.model.Result;
import com.pixivic.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotBlank;
import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SearchController {
    private final SearchService searchService;

    @GetMapping("/illusts")
    public Mono<ResponseEntity<Result<Illustration[]>>> search(@RequestParam @NotBlank String keyword, @NotBlank @RequestParam Integer page) throws UnsupportedEncodingException {
        return searchService.search(keyword, page).map(pixivResponse -> ResponseEntity.ok(new Result<>("拉取搜索结果成功", pixivResponse.getIllusts())));
    }

    @GetMapping("/illusts/{startDate}/{endDate}")
    public Mono<ResponseEntity<Result<Illustration[]>>> searchLimitByDate(@RequestParam String keyword, @RequestParam Integer page, @PathVariable String startDate, @PathVariable String endDate) throws UnsupportedEncodingException {
        return searchService.search(keyword, page, startDate, endDate).map(pixivResponse -> ResponseEntity.ok(new Result<>("拉取搜索结果成功", pixivResponse.getIllusts())));
    }

    @GetMapping("/candidates")
    public Mono<ResponseEntity<Result<String[]>>> getCandidateWords(@RequestParam @NotBlank String keyword) {
        return searchService.getCandidateWords(keyword).map(candidateWords -> ResponseEntity.ok(new Result<>("拉取候选词成功", candidateWords)));
    }
    @GetMapping("/relatedTags")
    public Mono<ResponseEntity<Result<RelatedTag[]>>> getRelatedTags(@RequestParam @NotBlank String keyword) throws UnsupportedEncodingException {
        return searchService.getRelatedTags(keyword).map(candidateWords -> ResponseEntity.ok(new Result<>("拉取候选词成功", candidateWords)));
    }

}
