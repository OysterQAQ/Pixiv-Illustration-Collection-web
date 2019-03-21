package com.pixivic.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pixivic.model.illust.MetaPage;
import com.pixivic.model.illust.MetaSinglePage;
import com.pixivic.model.illust.Tag;
import com.pixivic.model.illust.User;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Illustration {
    private String id;
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
    private Integer rank;
    private Integer sanity_level;//色情指数(大于5上传其他图床)
    private MetaSinglePage meta_single_page;
    private ArrayList<MetaPage> meta_pages;

}
