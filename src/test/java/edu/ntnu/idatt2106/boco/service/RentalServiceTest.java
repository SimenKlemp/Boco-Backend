package edu.ntnu.idatt2106.boco.service;

import edu.ntnu.idatt2106.boco.models.Image;
import edu.ntnu.idatt2106.boco.models.Item;
import edu.ntnu.idatt2106.boco.models.Rental;
import edu.ntnu.idatt2106.boco.models.User;
import edu.ntnu.idatt2106.boco.payload.request.RegisterRentalRequest;
import edu.ntnu.idatt2106.boco.payload.response.RentalResponse;
import edu.ntnu.idatt2106.boco.repository.RentalRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class RentalServiceTest {

    @Mock
    private RentalRepository rentalRepository;

    @Autowired
    @InjectMocks
    private RentalService rentalService;

    private RegisterRentalRequest request1;
    private RegisterRentalRequest request2;
    List<Rental> rentalList;

    Rental rental1;

    Item item;

    @BeforeEach
    public void setUp(){
        rentalList=new ArrayList<>();
        User user =new User("name",true,"address"
                ,"email","password","role",
                new Image());

        item =new Item();

        request1 =new RegisterRentalRequest("message", new Date(),new Date(),1l,1l);
        request2 =new RegisterRentalRequest("message", new Date(),new Date(),1l,1l);

        rental1 =new Rental(request1.getMessage(),request1.getStartDate(),
                request1.getEndDate(),"status1",user,item);

        rental1 =new Rental(request2.getMessage(),request2.getStartDate(),
                request2.getEndDate(),"status1",user,item);
        rentalList.add(rental1);
        rentalList.add(rental1);

    }

    @AfterEach
    public void tearDown(){
        request1 = request2=null;
        rentalList=null;
    }

    @Test
    void registerRental() throws Exception
    {
        Mockito.when(rentalRepository.save(Mockito.any(Rental.class))).thenReturn(rental1);
        rentalService.registerRental(request1);
        verify(rentalRepository,times(1)).save(Mockito.any(Rental.class));
    }

    @Test
    void getAllRentalsForItem() {

        rentalRepository.save(rental1);
       Mockito.when(rentalRepository.findAll()).thenReturn(rentalList);
        List<RentalResponse> rentalList1=rentalService.getAllRentalsForItem(item.getItemId());
        assertEquals(rentalList1,rentalList);
        verify(rentalRepository,times(1)).save(rental1);
        verify(rentalRepository,times(1)).findAll();

    }

    @Test
    void acceptRental() {

    }

    @Test
    void rejectRental() {
    }

    @Test
    void cancelRental() {
       Mockito.when (rentalService.cancelRental(rental1.getRentalId()));
       verify(rentalRepository,times(1)).findAll();
    }

    @Test
    void getRental() {
        Mockito.when(rentalRepository.findById(1L)).thenReturn(Optional.ofNullable(rental1));
        assertThat(rentalService.getRental(rental1.getRentalId())).isEqualTo(rental1);
    }
}