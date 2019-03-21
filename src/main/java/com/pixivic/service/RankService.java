package com.pixivic.service;

import com.pixivic.model.Rank;
import com.pixivic.repository.RankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Service
@RequestMapping("/ranks")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RankService {
    private final RankRepository rankRepository;
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public Mono<Rank> save(Rank rank) {
        return rankRepository.save(rank);
    }

    public Mono<Rank> getRankByModeAndDate(String mode, String date,Integer page) {
        Query query = Query.query(Criteria.where("mode").is(mode).and("date").is(date));
        query.fields().slice("illustrations", page* 30, 30);
        return reactiveMongoTemplate.findOne(query, Rank.class);
    }
}
