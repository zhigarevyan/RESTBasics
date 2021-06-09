package by.zhigarev.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Gift {
    private Integer id;
    private String name;
    private String description;
    private Integer price;
    private Integer duration;
    private LocalDate createDate;
    private LocalDate lastUpdateDate;
}
