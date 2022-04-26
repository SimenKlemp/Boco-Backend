package edu.ntnu.idatt2106.boco.repository;

import edu.ntnu.idatt2106.boco.models.Image;
import edu.ntnu.idatt2106.boco.models.Item;
import edu.ntnu.idatt2106.boco.models.Rental;
import edu.ntnu.idatt2106.boco.models.User;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;


@ExtendWith(MockitoExtension.class)
class RentalRepositoryTest {

    @Mock
    private RentalRepository rentalRepository;


    private Rental rental1;
    private Rental rental2;
    List<Rental> rentalList;

    @BeforeEach
    public void setUp(){
        rentalList=new ArrayList<>();
        User user =new User("name",true,"address"
                ,"email","password","role",
                new Image());

        Item item =new Item();
        rental1 =new Rental("message1",new Date(),
                new Date(),"status1",user,item,1);

        rentalList.add(rental1);
        rentalList.add(rental2);

    }

    @AfterEach
    public void tearDown(){
        rental1 = rental2=null;
        rentalList=null;
    }

    @Test
    void saveRental() {
        rentalRepository.save(rental1);
        Optional<Rental> fetchRental = rentalRepository.findById(rental1.getRentalId());
        assertEquals(1, Optional.ofNullable(fetchRental.get().getRentalId()));
    }

    @Test
    void getAllRentalsForItem() {
        rentalRepository.save(rental1);
        rentalRepository.save(rental2);

        rentalList=rentalRepository.findAll();
        assertEquals("message1",rentalList.get(1).getMessage());
    }
    @Test
    void cancelRental() {
        rentalRepository.save(rental1);
        rentalRepository.deleteById(rental1.getRentalId());
        Optional optional =rentalRepository.findById(rental1.getRentalId());
        assertEquals(Optional.empty(),optional);
    }

    @Test
    void getRental() {
        Rental rental=rentalRepository.save(rental1);
        Optional<Rental> optional=rentalRepository.findById(rental1.getRentalId());
        assertEquals(rental1.getRentalId(),optional.get().getRentalId());
        assertEquals(rental.getMessage(),optional.get().getMessage());
    }
}