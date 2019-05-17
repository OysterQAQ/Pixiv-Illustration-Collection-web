package com.pixivic.controller;

import com.pixivic.exception.CheckWordException;
import com.pixivic.exception.RankEmptyException;
import com.pixivic.model.Rank;
import com.pixivic.model.Result;
import com.pixivic.service.IllustrationService;
import com.pixivic.service.RankService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/ranks")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RankController {
    private final RankService rankService;
    private final IllustrationService illustrationService;
    @Value("${client.checkWord}")
    private String checkWord;

    @PostMapping
    public Mono<ResponseEntity<Result<Rank>>> acceptRankPost(@RequestBody() @Validated Rank rank, @RequestHeader String word) {
        if (checkWord.equals(word))
            return illustrationService.save(rank.getIllustrations(),rank.getDate()).then(rankService.save(rank)).map(r -> ResponseEntity.ok(new Result<>("保存成功", r)));
        throw new CheckWordException(new Result<>(HttpStatus.BAD_REQUEST, "校验码出错"));
    }

    @GetMapping
    public Mono<ResponseEntity<Result<Rank>>> getRank(@RequestParam @NotBlank String mode, @RequestParam @NotBlank String date, @RequestParam Integer page) {
        return rankService.getRankByModeAndDate(mode, date, page).map(rank -> {
            if (rank != null)
                return ResponseEntity.ok(new Result<>("拉取成功", rank));
            throw new RankEmptyException(new Result<>(HttpStatus.BAD_REQUEST, "当前选择条件下无排行信息"));
        });
    }

}
