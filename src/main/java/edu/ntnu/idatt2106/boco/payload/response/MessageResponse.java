package edu.ntnu.idatt2106.boco.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse
{
    private Long messageId;
    private String text;
    private Boolean isByUser;
    private Long userId;
    @JsonFormat(timezone = "GMT+02:00")
    private Date date;
}
