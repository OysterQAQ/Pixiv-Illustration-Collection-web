package com.pixivic.model;

import lombok.Data;

@Data
public class Comment {
    private String pid;
    private String id;
    private String user;
    private String email;
    private String content;
    private String time;
}
