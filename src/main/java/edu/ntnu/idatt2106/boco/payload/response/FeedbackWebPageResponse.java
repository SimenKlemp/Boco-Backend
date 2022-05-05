package edu.ntnu.idatt2106.boco.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class FeedbackWebPageResponse
{
    private Long feedbackId;
    private String message;
    private UserResponse user;
}
