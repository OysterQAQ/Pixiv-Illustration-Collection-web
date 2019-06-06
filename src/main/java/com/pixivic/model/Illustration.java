package com.pixivic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.pixivic.model.illust.MetaPage;
import com.pixivic.model.illust.MetaSinglePage;
import com.pixivic.model.illust.Tag;
import com.pixivic.model.illust.User;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "illustration")
public class Illustration {
    private String id;
    private String illust_id;//pixiv_id
    private String title;
    private String type;
    private String caption;
    private User user;
    private ArrayList<Tag> tags;
    private ArrayList<String> tools;
    private Date create_date;
    private Integer page_count;
    private Integer width;
    private Integer height;
    private Float height_width_ratio;//长宽比
    private Integer rank;
    private String dateOfThisRank;//当前所在排行的日期
    private Integer sanity_level;//色情指数(大于5上传其他图床)
    private MetaSinglePage meta_single_page;
    private ArrayList<MetaPage> meta_pages;

}
