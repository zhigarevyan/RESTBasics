package com.epam.esm.util;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthRequest {

    private String login;
    private String password;

}
