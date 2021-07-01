package com.epam.esm.util;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserEntityToDTOMapper {

    public static UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userDTO.getId());
        userDTO.setName(userDTO.getName());
        return userDTO;
    }

    public static List<UserDTO> toDTO(List<User> userList) {
        List<UserDTO> userDTOList = new ArrayList<>();
        userList.forEach(user -> userDTOList.add(toDTO(user)));
        return userDTOList;
    }
}
