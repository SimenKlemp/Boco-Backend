package edu.ntnu.idatt2106.boco.payload.request;

import edu.ntnu.idatt2106.boco.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackWebPageRequest {

    private String message;

    @NotBlank
    private Long userId;

}
