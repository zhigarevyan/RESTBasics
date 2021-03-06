package com.epam.esm.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class TagDTO {
    private Integer id;
    @NotNull
    @Size(min = 1, max = 45)
    private String name;
}
