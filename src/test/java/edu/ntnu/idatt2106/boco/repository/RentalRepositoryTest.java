package edu.ntnu.idatt2106.boco.repository;

import edu.ntnu.idatt2106.boco.factories.modelFactroies.ItemFactory;
import edu.ntnu.idatt2106.boco.factories.modelFactroies.RentalFactory;
import edu.ntnu.idatt2106.boco.factories.modelFactroies.UserFactory;
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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;


@WebMvcTest(MockitoExtension.class)
class RentalRepositoryTest {

    @MockBean
    private RentalRepository rentalRepository;


    private RentalFactory rental1;
    private RentalFactory rental2;
    List<Rental> rentalList;
    private UserFactory user;


    @BeforeEach
    public void setUp() throws Exception {
        rentalList=new ArrayList<>();
        user =new UserFactory();


        rental1 =new RentalFactory();

        rentalList.add(rental1.getObject());
        rentalList.add(rental2.getObject());
    }

    @AfterEach
    public void tearDown(){
        rental1 = rental2=null;
        rentalList=null;
    }

    @Test
    void saveRental() throws Exception {
        rentalRepository.save(rental1.getObject());
        Optional<Rental> fetchRental = rentalRepository.findById(rental1.getObject().getRentalId());
        assertEquals(1, Optional.ofNullable(fetchRental.get().getRentalId()));
    }

    @Test
    void getAllRentalsForItem() throws Exception {
        rentalRepository.save(rental1.getObject());
        rentalRepository.save(rental2.getObject());

        rentalList=rentalRepository.findAll();
        //assertEquals("message1",rentalList.get(1).getMessage());
    }
    @Test
    void cancelRental() throws Exception {
        rentalRepository.save(rental1.getObject());
        rentalRepository.deleteById(rental1.getObject().getRentalId());
        Optional optional =rentalRepository.findById(rental1.getObject().getRentalId());
        assertEquals(Optional.empty(),optional);
    }

    @Test
    void getRental() throws Exception {
        Rental rental=rentalRepository.save(rental1.getObject());
        Optional<Rental> optional=rentalRepository.findById(rental1.getObject().getRentalId());
        assertEquals(rental1.getObject().getRentalId(),optional.get().getRentalId());
       // assertEquals(rental.getMessage(),optional.get().getMessage());
    }
}