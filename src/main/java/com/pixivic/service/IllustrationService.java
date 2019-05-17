package com.pixivic.service;

import com.pixivic.model.Illustration;
import com.pixivic.model.illust.ImageUrls;
import com.pixivic.model.illust.MetaSinglePage;
import com.pixivic.repository.IllustrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IllustrationService {
    private final IllustrationRepository illustrationRepository;
    private final ReactiveMongoTemplate reactiveMongoTemplate;
    private Random random = new Random(21);

    public Mono<String> getRandomIllustration(Boolean isOriginal, Boolean isR18, Integer width, Integer height, Integer rank, String startDate, String endDate) {
        Criteria sorter = Criteria.where("type").is("illust");
        if (rank != null)
            sorter.and("rank").lte(rank);
        if (width != null && height != null)
            sorter.and("height_width_ratio").gte((float) height / width - 0.01f).lte((float) height / width + 0.01f);
        if (isR18 != null) {
            if (isR18)
                sorter.and("sanity_level").gte(5);
            else
                sorter.and("sanity_level").lte(5);
        }
        final boolean UrlType = isOriginal == null ? true : isOriginal;
        if (startDate != null)
            sorter.and("dateOfThisRank").gte(startDate).lte(endDate);
        Aggregation aggregation = newAggregation(match(sorter), sample(1));
        Flux<Illustration> illust = reactiveMongoTemplate.aggregate(aggregation, "illust", Illustration.class);
        return illust.map(illustration -> {
            if (illustration.getPage_count() > 1) {
                ImageUrls image_urls = illustration.getMeta_pages().get(random.nextInt(illustration.getMeta_pages().size())).getImage_urls();
                return UrlType ? image_urls.getOriginal() : image_urls.getLarge();
            }
            MetaSinglePage meta_single_page = illustration.getMeta_single_page();
            return UrlType ? meta_single_page.getOriginal_image_url() : meta_single_page.getLarge_image_url();
        }).collectList().map(illustrations ->
                illustrations.size() != 0 ?
                        illustrations.get(0) :
                        "https://upload.cc/i1/2019/05/17/mDpx9X.jpg"
        );
    }

    public Flux<Illustration> save(List<Illustration> illustrations, String date) {
        illustrations.stream().parallel().forEach(illustration -> {
            illustration.setId(null);
         /*   illustration.setDateOfThisRank(date);
            illustration.setHeight_width_ratio((float) illustration.getHeight() / illustration.getWidth());
            if (illustration.getPage_count() > 1) {
                illustration.getMeta_pages().stream().forEach(metaPage -> {
                    ImageUrls image_urls = metaPage.getImage_urls();
                    image_urls.setOriginal(dealUrl(image_urls.getOriginal()));
                    image_urls.setLarge(dealUrl(image_urls.getLarge()));
                });
            } else {
                MetaSinglePage meta_single_page = illustration.getMeta_single_page();
                meta_single_page.setUrl(dealUrl(meta_single_page.getOriginal_image_url()), dealUrl(meta_single_page.getLarge_image_url()));
            }*/
        });

        return illustrationRepository.saveAll(illustrations);
    }

    private String dealUrl(String url) {
        if (url == null || url.startsWith("上传失败"))
            url = "https://upload.cc/i1/2019/05/17/ZyANYC.gif";
        return url.replace("i.pximg.net", "i.pximg.qixiv.me");
    }
}
