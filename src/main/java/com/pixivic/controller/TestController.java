package com.pixivic.controller;

import com.pixivic.repository.IllustrationRepository;
import com.pixivic.repository.RankRepository;
import com.pixivic.service.IllustrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TestController {

    private final RankRepository rankRepository;
    private final IllustrationRepository illustrationRepository;
    private final IllustrationService illustrationService;

   // @GetMapping
    public Mono<String> test() {
        return rankRepository.findAll().map(rank -> {
            return illustrationService.save(rank.getIllustrations()).subscribe();
        }).collectList().map(illustrationFlux -> "233");
    }
}
