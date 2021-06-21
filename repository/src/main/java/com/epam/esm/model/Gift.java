package com.epam.esm.model;

import lombok.Data;

import java.time.Instant;

@Data
public class Gift {
    private Integer id;
    private String name;
    private String description;
    private Integer price;
    private Integer duration;
    private Instant createDate;
    private Instant lastUpdateDate;
}
