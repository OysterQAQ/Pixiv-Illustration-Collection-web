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

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IllustrationService {
    private final IllustrationRepository illustrationRepository;
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public Mono<String> getRandomIllustration(Boolean isOriginal, Boolean isR18, Integer minWidth, String w_h_ratio, Float range, Integer rank, Boolean getDetail, String startDate, String endDate) {
        Criteria sorter = Criteria.where("type").is("illust");
        if (rank != null)
            sorter.and("rank").lte(rank);
        range = range != null ? range : 0.005f;
        if (w_h_ratio != null) {
            String[] w_h_r = w_h_ratio.split("-");
            System.out.println(w_h_r[0]+"&"+w_h_r[1]);
            sorter.and("height_width_ratio").gte(Float.valueOf(w_h_r[1]) / Float.valueOf(w_h_r[0]) - range).lte(Float.valueOf(w_h_r[1]) / Float.valueOf(w_h_r[0]) + range);
        }
        if (minWidth != null)
            sorter.and("width").gte(minWidth);
        if (isR18 != null) {
            if (isR18)
                sorter.and("sanity_level").gte(5);
            else
                sorter.and("sanity_level").lte(5);
        }
        if (startDate != null)
            sorter.and("dateOfThisRank").gte(startDate).lte(endDate);
        Aggregation aggregation = newAggregation(match(sorter), sample(1));
        Flux<Illustration> illust = reactiveMongoTemplate.aggregate(aggregation, "illustration", Illustration.class);
        return illust.map(illustration -> {
            String url;
            if (illustration.getPage_count() > 1) {
                ImageUrls image_urls = illustration.getMeta_pages().get(0).getImage_urls();
                url = (isOriginal == null ? true : isOriginal) ? image_urls.getOriginal() : image_urls.getLarge();
            } else {
                MetaSinglePage meta_single_page = illustration.getMeta_single_page();
                url = (isOriginal == null ? true : isOriginal) ? meta_single_page.getOriginal_image_url() : meta_single_page.getLarge_image_url();
            }
            return (getDetail != null ? getDetail : false) ? addDetail(url, illustration) : url;
        }).collectList().map(illustrations ->
                illustrations.size() != 0 ?
                        illustrations.get(0) :
                        "https://upload.cc/i1/2019/05/17/mDpx9X.jpg"
        );
    }

    public Mono<List<Illustration>> save(List<Illustration> illustrations) {
        illustrations.stream().parallel().forEach(illustration -> {
            //  illustration.setDateOfThisRank(date);
            //  illustration.setIllust_id(illustration.getId());
            illustration.setId(null);
/*            illustration.setHeight_width_ratio((float) illustration.getHeight() / illustration.getWidth());
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
        return illustrationRepository.insert(illustrations).collectList();
    }
/*

    private String dealUrl(String url) {
        if (url == null || url.startsWith("上传失败"))
            url = "https://upload.cc/i1/2019/05/17/ZyANYC.gif";
        return url.replace("i.pximg.net", "i.pximg.qixiv.me");
    }
*/

    private String addDetail(String url, Illustration illustration) {
        return new StringBuilder(url).append("?id=").append(illustration.getIllust_id())
                .append("&title=").append(URLEncoder.encode(illustration.getTitle(), StandardCharsets.UTF_8))
                .append("&artist_id=").append(illustration.getUser().getId())
                .append("&artist_name=").append(URLEncoder.encode(illustration.getUser().getName(), StandardCharsets.UTF_8)).toString();
    }
}
