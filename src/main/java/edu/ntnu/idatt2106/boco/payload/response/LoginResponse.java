package edu.ntnu.idatt2106.boco.payload.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse
{
    private String token;
    private UserResponse userInfo;
}
