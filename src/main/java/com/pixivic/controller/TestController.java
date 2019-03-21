package com.pixivic.controller;

import com.pixivic.model.Result;
import com.pixivic.util.OauthUtil;
import com.pixivic.util.SearchUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TestController {
    private final OauthUtil oauthUtil;
    private final SearchUtil searchUtil;

    @GetMapping("/233")
    public Mono<ResponseEntity<Result<String>>> test() throws UnsupportedEncodingException {
        System.out.println(oauthUtil.getAccess_token());
        searchUtil.search("saber", 0);
        return null;
    }
}
