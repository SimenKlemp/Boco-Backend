package edu.ntnu.idatt2106.boco.controllers;

import edu.ntnu.idatt2106.boco.payload.request.RegisterMessageRequest;
import edu.ntnu.idatt2106.boco.payload.response.MessageResponse;
import edu.ntnu.idatt2106.boco.payload.response.ChatResponse;
import edu.ntnu.idatt2106.boco.payload.response.RentalResponse;
import edu.ntnu.idatt2106.boco.service.MessageService;
import edu.ntnu.idatt2106.boco.service.RentalService;
import edu.ntnu.idatt2106.boco.token.TokenComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/chat")
@EnableAutoConfiguration
@CrossOrigin
public class ChatController
{
    @Autowired
    private MessageService messageService;

    @Autowired
    private RentalService rentalService;

    @Autowired
    private TokenComponent tokenComponent;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    Logger logger = LoggerFactory.getLogger(ChatController.class);

    @MessageMapping("/chat-incoming")
    public void handleMessage(@Payload RegisterMessageRequest request)
    {
        try
        {
            MessageResponse response = messageService.register(request);

            if (response == null)
            {
                throw new NullPointerException("chatService.handleMessage return null");
            }

            simpMessagingTemplate.convertAndSend( "/chat-outgoing/" + request.getRentalId(), response);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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

            ChatResponse response = messageService.getChat(rentalId);
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
