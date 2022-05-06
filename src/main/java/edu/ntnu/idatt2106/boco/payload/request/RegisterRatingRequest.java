package edu.ntnu.idatt2106.boco.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRatingRequest
{
    @NotBlank
    private String feedback;

    private double rate;

    @NotBlank
    private Long rentalId;

    @NotBlank
    private Long userId;


}
