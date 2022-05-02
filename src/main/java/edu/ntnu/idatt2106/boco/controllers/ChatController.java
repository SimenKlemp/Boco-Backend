package edu.ntnu.idatt2106.boco.controllers;

import edu.ntnu.idatt2106.boco.payload.response.ChatResponse;
import edu.ntnu.idatt2106.boco.payload.response.ItemResponse;
import edu.ntnu.idatt2106.boco.payload.response.RentalResponse;
import edu.ntnu.idatt2106.boco.service.RentalService;
import edu.ntnu.idatt2106.boco.token.TokenComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/chat")
@EnableAutoConfiguration
@CrossOrigin
public class ChatController
{
    @Autowired
    private RentalService rentalService;

    @Autowired
    private TokenComponent tokenComponent;

    @GetMapping("/{rentalId}")
    public ResponseEntity<ChatResponse> getChat(@PathVariable("rentalId") long rentalId)
    {
        try
        {
            RentalResponse rental = rentalService.getRental(rentalId);
            Long userId1 = rental.getUser().getUserId();
            Long userId2 = rental.getItem().getUser().getUserId();

            if (!tokenComponent.haveAccessTo(userId1) && !tokenComponent.haveAccessTo(userId2))
            {
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }

            ChatResponse response = rentalService.getChat(rentalId);
            if (response == null)
            {
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity("Error",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
