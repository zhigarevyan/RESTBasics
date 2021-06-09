package by.zhigarev.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class GiftDTO {
    private Integer id;
    private String name;
    private String description;
    private Integer price;
    private Integer duration;
    private LocalDate createDate;
    private LocalDate lastUpdateDate;
}
