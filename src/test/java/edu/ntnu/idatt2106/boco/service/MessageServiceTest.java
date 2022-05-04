package edu.ntnu.idatt2106.boco.service;

import edu.ntnu.idatt2106.boco.BocoApplication;
import edu.ntnu.idatt2106.boco.models.*;
import edu.ntnu.idatt2106.boco.payload.request.RegisterMessageRequest;
import edu.ntnu.idatt2106.boco.payload.response.ChatResponse;
import edu.ntnu.idatt2106.boco.payload.response.MessageResponse;
import edu.ntnu.idatt2106.boco.repository.ItemRepository;
import edu.ntnu.idatt2106.boco.repository.MessageRepository;
import edu.ntnu.idatt2106.boco.repository.RentalRepository;
import edu.ntnu.idatt2106.boco.repository.UserRepository;
import edu.ntnu.idatt2106.boco.util.ModelFactory;
import edu.ntnu.idatt2106.boco.util.RequestFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BocoApplication.class)
public class MessageServiceTest
{
    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Before
    public void before()
    {
        for (Message message : messageRepository.findAll())
        {
            messageService.delete(message.getMessageId());
        }
    }

    @Test
    public void registerMessageCorrect()
    {
        User user1 = ModelFactory.getUser(null);
        user1 = userRepository.save(user1);

        User user2 = ModelFactory.getUser(null);
        user2 = userRepository.save(user2);

        Item item = ModelFactory.getItem(null, user1);
        item = itemRepository.save(item);

        Rental rental = ModelFactory.getRental(user2, item);
        rental = rentalRepository.save(rental);

        RegisterMessageRequest request = RequestFactory.getRegisterMessageRequest(user2.getUserId(), rental.getRentalId());

        MessageResponse response = messageService.register(request);
        Message message = messageRepository.findById(response.getMessageId()).orElseThrow();

        assertThat(message.getMessageId()).isEqualTo(response.getMessageId());
        assertThat(message.getText()).isEqualTo(response.getText()).isEqualTo(request.getText());
        assertThat(message.getIsByUser()).isEqualTo(response.getIsByUser()).isTrue();
        assertThat(message.getUser().getUserId()).isEqualTo(response.getUserId()).isEqualTo(request.getUserId());
        assertThat(message.getRental().getRentalId()).isEqualTo(request.getRentalId());
    }

    @Test
    public void registerMessageWrongUserId()
    {
        User user1 = ModelFactory.getUser(null);
        user1 = userRepository.save(user1);

        User user2 = ModelFactory.getUser(null);
        user2 = userRepository.save(user2);

        Item item = ModelFactory.getItem(null, user1);
        item = itemRepository.save(item);

        Rental rental = ModelFactory.getRental(user2, item);
        rental = rentalRepository.save(rental);

        RegisterMessageRequest request = RequestFactory.getRegisterMessageRequest(3L, rental.getRentalId());

        MessageResponse response = messageService.register(request);

        assertThat(response).isNull();
    }

    @Test
    public void registerMessageWrongRentalId()
    {
        User user = ModelFactory.getUser(null);
        user = userRepository.save(user);

        RegisterMessageRequest request = RequestFactory.getRegisterMessageRequest(user.getUserId(), 1L);

        MessageResponse response = messageService.register(request);

        assertThat(response).isNull();
    }

    @Test
    public void registerMessageWrongUserIdForRentalId()
    {
        User user1 = ModelFactory.getUser(null);
        user1 = userRepository.save(user1);

        User user2 = ModelFactory.getUser(null);
        user2 = userRepository.save(user2);

        Item item = ModelFactory.getItem(null, user1);
        item = itemRepository.save(item);

        Rental rental = ModelFactory.getRental(user2, item);
        rental = rentalRepository.save(rental);

        User user3 = ModelFactory.getUser(null);
        user3 = userRepository.save(user3);

        RegisterMessageRequest request = RequestFactory.getRegisterMessageRequest(user3.getUserId(), rental.getRentalId());

        MessageResponse response = messageService.register(request);

        assertThat(response).isNull();
    }

    @Test
    public void getChatWithMessages()
    {
        User user1 = ModelFactory.getUser(null);
        user1 = userRepository.save(user1);

        User user2 = ModelFactory.getUser(null);
        user2 = userRepository.save(user2);

        Item item = ModelFactory.getItem(null, user1);
        item = itemRepository.save(item);

        Rental rental = ModelFactory.getRental(user2, item);
        rental = rentalRepository.save(rental);

        Message message1 = ModelFactory.getMessage(user2, rental);
        message1 = messageRepository.save(message1);
        Message message2 = ModelFactory.getMessage(user1, rental);
        message2 = messageRepository.save(message2);

        Message[] messages = {message1, message2};

        ChatResponse chatResponse = messageService.getChat(rental.getRentalId());

        assertThat(chatResponse.getRental().getRentalId()).isEqualTo(rental.getRentalId());
        for (int i = 0; i < messages.length; i++)
        {
            Message message = messages[i];
            MessageResponse response = chatResponse.getMessages().get(i);

            assertThat(message.getMessageId()).isEqualTo(response.getMessageId());
            assertThat(message.getText()).isEqualTo(response.getText());
            assertThat(message.getIsByUser()).isEqualTo(response.getIsByUser()).isTrue();
            assertThat(message.getUser().getUserId()).isEqualTo(response.getUserId());
            assertThat(message.getRental().getRentalId()).isEqualTo(rental.getRentalId());
        }
    }

    @Test
    public void getChatEmpty()
    {
        User user1 = ModelFactory.getUser(null);
        user1 = userRepository.save(user1);

        User user2 = ModelFactory.getUser(null);
        user2 = userRepository.save(user2);

        Item item = ModelFactory.getItem(null, user1);
        item = itemRepository.save(item);

        Rental rental = ModelFactory.getRental(user2, item);
        rental = rentalRepository.save(rental);

        ChatResponse chatResponse = messageService.getChat(rental.getRentalId());

        assertThat(chatResponse.getRental().getRentalId()).isEqualTo(rental.getRentalId());
        assertThat(chatResponse.getMessages()).isEmpty();
    }

    @Test
    public void getChatWrongRentalId()
    {
        ChatResponse chatResponse = messageService.getChat(0L);

        assertThat(chatResponse).isNull();
    }

    @Test
    public void deleteCorrect()
    {
        User user1 = ModelFactory.getUser(null);
        user1 = userRepository.save(user1);

        User user2 = ModelFactory.getUser(null);
        user2 = userRepository.save(user2);

        Item item = ModelFactory.getItem(null, user1);
        item = itemRepository.save(item);

        Rental rental = ModelFactory.getRental(user2, item);
        rental = rentalRepository.save(rental);

        Message message = ModelFactory.getMessage(user2, rental);
        message = messageRepository.save(message);

        boolean success = messageService.delete(message.getMessageId());
        Optional<Message> optionalMessage = messageRepository.findById(message.getMessageId());

        assertThat(success).isTrue();
        assertThat(optionalMessage.isEmpty()).isTrue();
    }

    @Test
    public void deleteWrongMessageId()
    {
        boolean success = messageService.delete(0L);
        assertThat(success).isFalse();
    }

}
