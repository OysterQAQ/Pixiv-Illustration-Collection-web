package com.pixivic.model.illust;

import lombok.Data;

@Data
public class MetaSinglePage {
    String original_image_url;
    String large_image_url;
    public void setUrl(String original_image_url,String large_image_url){
        this.original_image_url=original_image_url;
        this.large_image_url=large_image_url;
    }
}
