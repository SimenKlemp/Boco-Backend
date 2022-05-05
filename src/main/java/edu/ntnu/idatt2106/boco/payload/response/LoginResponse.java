package edu.ntnu.idatt2106.boco.payload.response;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class LoginResponse
{
    private String token;
    private UserResponse userInfo;

}
