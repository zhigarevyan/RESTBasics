package com.epam.esm.util;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
public class GiftQueryParameters {
    @Size(min = 1, max = 45)
    private String name;
    @Size(min = 1, max = 200)
    private String description;
    @Valid
    private List<Filter> price;
    @Valid
    private List<Filter> duration;
    private List<String> tagName;
    private Sort sort;


    public GiftQueryParameters(String name, String description, List<String> price,
                               List<String> duration, String sort, List<String> tagName) {
        if (!StringUtils.isEmpty(name)) {
            setName(name);
        }
        if (!StringUtils.isEmpty(description)) {
            setDescription(description);
        }
        if (price != null && !price.isEmpty()) {
            setPrice(price);
        }
        if (duration != null && !duration.isEmpty()) {
            setDuration(duration);
        }
        if (!StringUtils.isEmpty(sort)) {
            setSort(sort);
        }
        if (tagName != null && !tagName.isEmpty()) {
            this.tagName = tagName;
        }
    }

    public void setPrice(List<String> price) {
        this.price = FilterParser.createFilter(price);
    }

    public void setDuration(List<String> duration) {
        this.duration = FilterParser.createFilter(duration);
    }

    public void setSort(String sortBy) {
        sort = SortParser.createSort(sortBy);
    }

    public boolean isEmpty() {
        return name == null && description == null && price == null && duration == null &&
                tagName == null && sort == null;
    }
}
