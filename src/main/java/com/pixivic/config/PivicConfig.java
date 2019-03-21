package com.pixivic.config;

import com.mongodb.reactivestreams.client.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class PivicConfig {
    private final MongoClient mongoClient;

    @Autowired
    public PivicConfig(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @Bean
    public ReactiveMongoTemplate reactiveMongoTemplate() {
        return new ReactiveMongoTemplate(mongoClient, "pixiv-illustration-collection");
    }

    @Bean
    public WebClient webClient() {
        return WebClient
                .builder()
                .baseUrl("https://search.api.pixivic.com")
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.addAll(new LinkedMultiValueMap<>() {{
                        add("User-Agent", "PixivAndroidApp/5.0.93 (Android 5.1; m2)");
                        add("Content-Type", "application/x-www-form-urlencoded");
                        add("App-OS", "android");
                        add("App-OS-Version", "5.1");
                        add("App-Version", "5.0.93");
                        add("Accept-Language", "zh_CN");
                    }});
                })
                .build();
    }

    @Bean
    CorsWebFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("https://pixivic.com");
        config.addAllowedOrigin("https://m.picivic.com");
        config.addAllowedOrigin("http://localhost:63342");
        config.addAllowedHeader("*");
        config.addExposedHeader("Authorization");
        config.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsWebFilter(source);
    }
}
