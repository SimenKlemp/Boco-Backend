package edu.ntnu.idatt2106.boco.service;

import edu.ntnu.idatt2106.boco.models.Message;
import edu.ntnu.idatt2106.boco.models.Rental;
import edu.ntnu.idatt2106.boco.models.User;
import edu.ntnu.idatt2106.boco.payload.request.RegisterMessageRequest;
import edu.ntnu.idatt2106.boco.payload.response.ChatResponse;
import edu.ntnu.idatt2106.boco.payload.response.MessageResponse;
import edu.ntnu.idatt2106.boco.payload.response.RentalResponse;
import edu.ntnu.idatt2106.boco.repository.MessageRepository;
import edu.ntnu.idatt2106.boco.repository.RentalRepository;
import edu.ntnu.idatt2106.boco.repository.UserRepository;
import edu.ntnu.idatt2106.boco.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class MessageService
{
    @Autowired
    MessageRepository messageRepository;

    @Autowired
    RentalRepository rentalRepository;

    @Autowired
    UserRepository userRepository;

    /**
     * A method for storing messages in database
     *
     * @param request the message that is being sent
     * @return returns a MessageResponse
     */
    public MessageResponse register(RegisterMessageRequest request)
    {
        Optional<User> optionalUser = userRepository.findById(request.getUserId());
        if (optionalUser.isEmpty()) return null;
        User user = optionalUser.get();

        Optional<Rental> optionalRental = rentalRepository.findById(request.getRentalId());
        if (optionalRental.isEmpty()) return null;
        Rental rental = optionalRental.get();

        if (!Objects.equals(rental.getUser().getUserId(), user.getUserId()) && !Objects.equals(rental.getItem().getUser().getUserId(), user.getUserId()))
        {
            return null;
        }

        Message message = new Message(request.getText(), true, new Date(), user, rental);
        message = messageRepository.save(message);

        return Mapper.ToMessageResponse(message);
    }

    /**
     * A method for retrieving all messages belonging a rentalId
     *
     * @param rentalId the rentalId that the messages belong to
     * @return returns a ChatResponse which contains information about both rental and message object.
     */
    public ChatResponse getChat(long rentalId)
    {
        Optional<Rental> optionalRental = rentalRepository.findById(rentalId);
        if (optionalRental.isEmpty()) return null;
        Rental rental = optionalRental.get();

        List<Message> messages = messageRepository.findAllByRental(rental, Sort.by("date"));

        RentalResponse rentalResponse = Mapper.ToRentalResponse(rental);
        List<MessageResponse> messageResponses = Mapper.ToMessageResponses(messages);

        return new ChatResponse(rentalResponse, messageResponses);
    }

    public boolean delete(Long messageId)
    {
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if (optionalMessage.isEmpty()) return false;
        Message message = optionalMessage.get();

        messageRepository.delete(message);
        return true;
    }

}
