package com.pixivic.model.illust;

import lombok.Data;

@Data
public class User {
    private String id;
    private String name;
    private String account;
    private ProfileImageUrls profile_image_urls;
}
