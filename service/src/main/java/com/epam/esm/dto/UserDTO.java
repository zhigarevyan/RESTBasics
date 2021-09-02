package com.epam.esm.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserDTO {

    private Integer id;
    private String name;
    private String login;
    private List<UserOrderDTO> orderList;
}
