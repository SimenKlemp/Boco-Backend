package edu.ntnu.idatt2106.boco.service;

import edu.ntnu.idatt2106.boco.models.Item;
import edu.ntnu.idatt2106.boco.models.Message;
import edu.ntnu.idatt2106.boco.models.Rental;
import edu.ntnu.idatt2106.boco.models.User;
import edu.ntnu.idatt2106.boco.payload.request.RegisterMessageRequest;
import edu.ntnu.idatt2106.boco.payload.response.ChatResponse;
import edu.ntnu.idatt2106.boco.payload.response.MessageResponse;
import edu.ntnu.idatt2106.boco.repository.MessageRepository;
import edu.ntnu.idatt2106.boco.repository.RentalRepository;
import edu.ntnu.idatt2106.boco.repository.UserRepository;
import edu.ntnu.idatt2106.boco.util.ModelFactory;
import edu.ntnu.idatt2106.boco.util.RepositoryMock;
import edu.ntnu.idatt2106.boco.util.RequestFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class ChatServiceTest
{
    @InjectMocks
    private ChatService chatService;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RentalRepository rentalRepository;

    @BeforeEach
    public void beforeEach()
    {
        RepositoryMock.mockMessageRepository(messageRepository);
        RepositoryMock.mockUserRepository(userRepository);
        RepositoryMock.mockRentalRepository(rentalRepository);
    }

    @Test
    public void registerMessageCorrect()
    {
        User user1 = ModelFactory.getUser(null);
        user1 = userRepository.save(user1);

        User user2 = ModelFactory.getUser(null);
        user2 = userRepository.save(user2);

        Item item = ModelFactory.getItem(null, user1);
        Rental rental = ModelFactory.getRental(user2, item);
        rental = rentalRepository.save(rental);

        RegisterMessageRequest request = RequestFactory.getRegisterMessageRequest(user2.getUserId(), rental.getRentalId());

        MessageResponse response = chatService.register(request);
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
        Rental rental = ModelFactory.getRental(user2, item);
        rental = rentalRepository.save(rental);

        RegisterMessageRequest request = RequestFactory.getRegisterMessageRequest(3L, rental.getRentalId());

        MessageResponse response = chatService.register(request);

        assertThat(response).isNull();
    }

    @Test
    public void registerMessageWrongRentalId()
    {
        User user = ModelFactory.getUser(null);
        user = userRepository.save(user);

        RegisterMessageRequest request = RequestFactory.getRegisterMessageRequest(user.getUserId(), 1L);

        MessageResponse response = chatService.register(request);

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
        Rental rental = ModelFactory.getRental(user2, item);
        rental = rentalRepository.save(rental);

        User user3 = ModelFactory.getUser(null);
        user3 = userRepository.save(user3);

        RegisterMessageRequest request = RequestFactory.getRegisterMessageRequest(user3.getUserId(), rental.getRentalId());

        MessageResponse response = chatService.register(request);

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

        Rental rental = ModelFactory.getRental(user2, item);
        rental = rentalRepository.save(rental);

        Message message1 = ModelFactory.getMessage(user2, rental);
        message1 = messageRepository.save(message1);
        Message message2 = ModelFactory.getMessage(user1, rental);
        message2 = messageRepository.save(message2);

        Message[] messages = {message1, message2};

        ChatResponse chatResponse = chatService.getChat(rental.getRentalId());

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

        Rental rental = ModelFactory.getRental(user2, item);
        rental = rentalRepository.save(rental);

        ChatResponse chatResponse = chatService.getChat(rental.getRentalId());

        assertThat(chatResponse.getRental().getRentalId()).isEqualTo(rental.getRentalId());
        assertThat(chatResponse.getMessages()).isEmpty();
    }

    @Test
    public void getChatWrongRentalId()
    {
        ChatResponse chatResponse = chatService.getChat(1L);

        assertThat(chatResponse).isNull();
    }
}
