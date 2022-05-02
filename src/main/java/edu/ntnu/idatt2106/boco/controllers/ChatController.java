package edu.ntnu.idatt2106.boco.controllers;

import edu.ntnu.idatt2106.boco.payload.request.MessageRequest;
import edu.ntnu.idatt2106.boco.payload.response.MessageResponse;
import edu.ntnu.idatt2106.boco.payload.response.ChatResponse;
import edu.ntnu.idatt2106.boco.payload.response.RentalResponse;
import edu.ntnu.idatt2106.boco.service.RentalService;
import edu.ntnu.idatt2106.boco.token.TokenComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

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

    Logger logger = LoggerFactory.getLogger(ChatController.class);

    @MessageMapping("/chat-incoming")
    @SendTo("/chat-outgoing")
    public MessageResponse send(MessageRequest request) throws Exception
    {
        return new MessageResponse(request.getText(), true, request.getUserId());
    }

    @GetMapping("/get/{rentalId}")
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
