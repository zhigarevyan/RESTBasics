package com.epam.esm.util;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class Filter {
    private FilterType type;
    @Min(0)
    private Integer value;
}
