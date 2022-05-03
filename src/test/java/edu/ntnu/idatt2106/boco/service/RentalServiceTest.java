package edu.ntnu.idatt2106.boco.service;

import edu.ntnu.idatt2106.boco.factories.modelFactroies.ItemFactory;
import edu.ntnu.idatt2106.boco.factories.modelFactroies.MessageFactory;
import edu.ntnu.idatt2106.boco.factories.modelFactroies.RentalFactory;
import edu.ntnu.idatt2106.boco.factories.modelFactroies.UserFactory;
import edu.ntnu.idatt2106.boco.factories.requestFactroies.RegisterRentalRequestFactory;
import edu.ntnu.idatt2106.boco.models.Item;
import edu.ntnu.idatt2106.boco.models.Message;
import edu.ntnu.idatt2106.boco.models.Rental;
import edu.ntnu.idatt2106.boco.models.User;
import edu.ntnu.idatt2106.boco.payload.request.RegisterRentalRequest;
import edu.ntnu.idatt2106.boco.payload.response.RentalResponse;
import edu.ntnu.idatt2106.boco.repository.*;
import edu.ntnu.idatt2106.boco.util.RepositoryMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class RentalServiceTest {
    @InjectMocks
    private RentalService rentalService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private MessageRepository messageRepository;


    private User user =new User();

    private Item item1 =new Item();
    private Item item2=new Item();


    private Rental rental1=new Rental();
    private Rental rental2=new Rental();

    private RegisterRentalRequest registerRentalRequest1;
    RentalResponse response =new RentalResponse();
    List<RentalResponse>rentalResponse=new ArrayList<>();


    @BeforeEach
    public void beforeEach () throws Exception {
        RepositoryMock.mockUserRepository(userRepository);
        RepositoryMock.mockImageRepository(imageRepository);
        RepositoryMock.mockItemRepository(itemRepository);
        RepositoryMock.mockRentalRepository(rentalRepository);
        RepositoryMock.mockMessageRepository(messageRepository);

        rental1=new RentalFactory().getObject();
        assert rental1 != null;
        rental2=new RentalFactory().getObject();
        assert rental2 != null;
        item1=new ItemFactory().getObject();
        assert item1 != null;
        item2=new ItemFactory().getObject();
        assert item2 != null;

        user=new UserFactory().getObject();
        assert user != null;


        rental1.setUser(user);
        rental2.setUser(user);

        rental1.setItem(item1);
        rental2.setItem(item1);

        userRepository.save(user);
        itemRepository.save(item1);
        itemRepository.save(item2);
        rentalRepository.save(rental1);
        rentalRepository.save(rental2);


    }
    @AfterEach
    public void cleanUp () {
        itemRepository.deleteAll();
        rentalRepository.deleteAll();
        userRepository.deleteAll();
    }

        @Test
        public void registerRentalWithCorrectUserIdTest () throws Exception {
            registerRentalRequest1 = new RegisterRentalRequest(
                    "message",
                    new Date(),
                    new Date(),
                    user.getUserId(),
                    item1.getItemId(),
                    null
            );
            assert registerRentalRequest1 != null;
            RentalResponse rentalResponse = rentalService.registerRental(registerRentalRequest1);

            assertThat(rentalResponse.getUser().getUserId()).isEqualTo(registerRentalRequest1.getUserId());
            assertThat(rentalResponse.getItem().getItemId()).isEqualTo(registerRentalRequest1.getItemId());
    }
    @Test
    public void registerRentalWithWrongUserIdTest ()
    {
        registerRentalRequest1 = new RegisterRentalRequest(
                "message",
                 new Date(),
                 new Date(),
                5L,
                 item1.getItemId(),
                null
        );

        assert registerRentalRequest1 != null;

        RentalResponse rentalResponse = rentalService.registerRental(registerRentalRequest1);
        assertThat(rentalResponse).isNull();

    }

    @Test
    public void registerRentalWithWrongItemIdTest ()
    {
        registerRentalRequest1 = new RegisterRentalRequest(
                "message",
                 new Date(),
                 new Date(),
                 user.getUserId(),
                5L,
                null
        );
        assert registerRentalRequest1 != null;
        RentalResponse rentalResponse = rentalService.registerRental(registerRentalRequest1);
        assertThat(rentalResponse).isNull();
    }

    @Test
    public void registerRentalWithWrongDeliveryInfoTest () {

        item1.setIsDeliverable(false);
        itemRepository.save(item1);
        registerRentalRequest1 = new RegisterRentalRequest(
                "message",
                new Date(),
                new Date(),
                user.getUserId(),
                item1.getItemId(),
                Rental.DeliverInfo.DELIVERED
        );
        assert registerRentalRequest1 != null;
        RentalResponse rentalResponse = rentalService.registerRental(registerRentalRequest1);
        assertThat(rentalResponse).isNull();
    }

    @Test
    public void getAllForAllRentalsForSpecificItemTest () {

            Item item = itemRepository.findById(item1.getItemId()).get();
            rentalResponse = rentalService.getAllRentalsForItem(item.getItemId());
            assertThat(rentalResponse.size()).isEqualTo(2);
    }

    @Test
    public void retunEmptyRentalListForSpecifcItem () throws Exception{

        Item item3=new ItemFactory().getObject();
        itemRepository.save(item3);
        Item item = itemRepository.findById(item3.getItemId()).get();
        rentalResponse = rentalService.getAllRentalsForItem(item .getItemId());
        assertThat(rentalResponse.size()).isZero();

    }

    @Test
    public void retunNullForRentalListWithWrongItemId () {
        rentalResponse = rentalService.getAllRentalsForItem(5L);
        assertThat(rentalResponse).isNull();
    }

    @Test
    public void returnFINISHEDStatusForAcceptedRentalsTest () {
        Rental rental =rentalRepository.findById(rental1.getRentalId()).get();
        response= rentalService.acceptRental(rental1.getRentalId());
        assertThat(response.getStatus()).isEqualTo("FINISHED");
    }

        @Test
        public void returnNullForAcceptRentalForWrongItemId ()
        {
            RentalResponse response =new RentalResponse();
            response= rentalService.acceptRental(5L);
            assertThat(response).isNull();
        }

        @Test
        public void rejectAllRentalsWithStatusREJECTEDTest ()
        {
            Rental rental =rentalRepository.findById(rental1.getRentalId()).get();
            RentalResponse response =new RentalResponse();
            response= rentalService.rejectRental(rental1.getRentalId());
            assertThat(response.getStatus()).isEqualTo("REJECTED");
        }

        @Test
        public void returnNullForRentalsWithREJECTStatusWithWrongItemId()
        {
            response= rentalService.rejectRental(5L);
            assertThat(response).isNull();
        }

        @Test
        public void cancelAllRentalsWithCANCELStatusTest ()
        {
            Rental rental =rentalRepository.findById(rental1.getRentalId()).get();
            response= rentalService.cancelRental(rental1.getRentalId());
            assertThat(response.getStatus()).isEqualTo("CANCELED");
        }

        @Test
        public void cancelAllRentalsWithCANCELStatusAndWithWrongRentalIdTest ()
        {
            response= rentalService.cancelRental(5L);
            assertThat(response).isNull();
        }

        @Test
        public void getSpecificRentalByUsingRentalId ()
        {
            Rental rental = rentalRepository.findById(rental1.getRentalId()).get();
            response=rentalService.getRental(rental.getRentalId());
            assertThat(response.getRentalId()).isEqualTo(rental.getRentalId());
        }

        @Test
        public void getWrongRentalId ()
        {
            response=rentalService.getRental(4L);
            assertThat(response).isNull();
        }

        @Test
        public void getAllRentalForUserWithIdAndACCEPTEDStatus ()
        {
            rental1.setStatus(Rental.Status.ACCEPTED);
            rental2.setStatus(Rental.Status.ACCEPTED);
            rentalRepository.save(rental1);
            rentalRepository.save(rental2);
            User user1 = userRepository.findById(user.getUserId()).get();
            rentalResponse = rentalService.getAllRentalsUser(user1.getUserId(), Rental.Status.ACCEPTED);
            assertThat(rentalResponse.size()).isEqualTo(2);
        }

        @Test
        public void getEmptyListRentalsForSpecificUserTest () throws Exception {
            User user2=new UserFactory().getObject();
            userRepository.save(user2);
            User user3 = userRepository.findById(user2.getUserId()).get();
            rentalResponse = rentalService.getAllRentalsUser(user3.getUserId(), Rental.Status.PENDING);
            assertThat(rentalResponse.size()).isZero();
        }

        @Test
        public void returnNullRentalListForWrongUserId ()
        {
            rentalResponse = rentalService.getAllRentalsUser(5L, Rental.Status.ACCEPTED);
            assertThat(rentalResponse).isNull();
        }
    }

