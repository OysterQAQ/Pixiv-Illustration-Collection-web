package com.pixivic.controller;

import com.pixivic.service.IllustrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Validated
@RestController
@RequestMapping("/illust")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IllustrationController {
    private final IllustrationService illustrationService;

    @GetMapping
    public Mono<ResponseEntity<String>> random(Boolean isOriginal, Boolean isR18, Integer width, Integer height, @Max(140) @Min(0) Integer rank) {
        return illustrationService.getRandomIllustration(isOriginal, isR18, width, height, rank, null, null).map(s -> ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header("Location", s).header("Cache-Control", "no-cache").body(null));
    }

    @GetMapping("/{startDate}/{endDate}")
    public Mono<ResponseEntity<String>> randomByDate(@PathVariable String startDate, @PathVariable String endDate, Boolean isOriginal, Boolean isR18, Integer width, Integer height, @Max(140) @Min(0) Integer rank) {
        return illustrationService.getRandomIllustration(isOriginal, isR18, width, height, rank, startDate, endDate).map(s -> ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header("Location", s).header("Cache-Control", "no-cache").body(null));
    }
}
