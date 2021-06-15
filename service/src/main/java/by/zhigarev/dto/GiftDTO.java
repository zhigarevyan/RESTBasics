package by.zhigarev.dto;

import by.zhigarev.model.Tag;
import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
public class GiftDTO {
    private Integer id;
    private String name;
    private String description;
    private Integer price;
    private Integer duration;
    private Instant createDate;
    private Instant lastUpdateDate;
    private List<String> tagList;

    public void setTagList(List<Tag> tagList) {
        this.tagList = new ArrayList<>();
        tagList.forEach(tag -> this.tagList.add(tag.getName()));
    }
}
