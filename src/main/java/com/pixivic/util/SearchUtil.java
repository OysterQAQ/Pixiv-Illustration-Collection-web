package com.pixivic.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pixivic.model.Candidate;
import com.pixivic.model.PixivResponse;
import com.pixivic.model.RelatedTag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.HtmlUtils;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SearchUtil {
    private final WebClient webClient;
    private final OauthUtil oauthUtil;
    private ObjectMapper objectMapper;

    {
        objectMapper = new ObjectMapper();
    }

    public Mono<PixivResponse> search(String keyword, Integer page, String startDate, String endDate) throws UnsupportedEncodingException {
        return request("http://app-api.pixivlite.com/v1/search/illust?filter=for_android&sort=popular_desc&search_target=partial_match_for_tags&start_date=" + startDate + "&end_date=" + endDate + "&word=" + URLEncoder.encode(keyword, "UTF-8") + "&offset=" + page * 30, PixivResponse.class).cast(PixivResponse.class);
    }

    public Mono<PixivResponse> search(String keyword, Integer page) throws UnsupportedEncodingException {
        return request("http://app-api.pixivlite.com/v1/search/illust?filter=for_android&sort=popular_desc&search_target=partial_match_for_tags&word=" + URLEncoder.encode(keyword, "UTF-8") + "&offset=" + page * 30, PixivResponse.class).cast(PixivResponse.class);
    }

    private Mono request(String url, Class clazz) {
        return
                webClient.get()
                        .uri(URI.create(url))
                        .headers(httpHeaders -> {
                            try {
                                String[] hash = oauthUtil.gethash();
                                httpHeaders.addAll(new LinkedMultiValueMap<>() {{
                                    add("Authorization", "Bearer " + oauthUtil.getAccess_token());
                                    add("X-Client-Time", hash[0]);
                                    add("X-Client-Hash", hash[1]);
                                }});
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            }
                        })
                        .retrieve()
                        .bodyToMono(clazz);
    }

    public Mono<Candidate> getCandidateWords(String keyword) {
        return request("http://app-api.pixivlite.com/v1/search/autocomplete?word=" + keyword, Candidate.class).cast(Candidate.class);
    }

    public Mono<RelatedTag[]> getRelatedTags(String keyword) throws UnsupportedEncodingException {
        return
                WebClient.create().get()
                        .uri(URI.create("https://tag.api.pixivic.com/search.php?s_mode=s_tag&word=" + URLEncoder.encode(keyword, "UTF-8")))
                        .header("accept-language", "zh-CN,zh;q=0.9")
                        .accept(MediaType.TEXT_HTML)
                        .retrieve()
                        .bodyToMono(String.class).map(s -> {
                    try {
                        return objectMapper.readValue(HtmlUtils.htmlUnescape(s.substring(s.indexOf("data-related-tags") + 19, s.indexOf("\"data-tag"))), RelatedTag[].class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    throw new RuntimeException();
                });
    }
}
