package com.pixivic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@Data
@Document(collection = "comment")
public class Comment {
    @NotBlank
    private String pid;
    @Id
    private String id;
    @NotBlank
    private String user;
    @NotBlank@JsonIgnore
    private String email;
    @NotBlank
    private String content;
    private String time;

    @JsonProperty
    public void setEmail(String email) {
        this.email = email;
    }
}
