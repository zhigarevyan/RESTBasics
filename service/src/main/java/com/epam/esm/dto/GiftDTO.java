package com.epam.esm.dto;

import com.epam.esm.model.Tag;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class GiftDTO {
    private Integer id;
    private String name;
    private String description;
    private Integer price;
    private Integer duration;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime createDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime lastUpdateDate;
    private List<String> tagList;

    public void setTagListWithTags(List<Tag> tagList) {
        this.tagList = new ArrayList<>();
        tagList.forEach(tag -> this.tagList.add(tag.getName()));
    }

    public void setTagList(List<String> tagList){
        this.tagList = tagList;
    }
}
