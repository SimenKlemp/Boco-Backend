package edu.ntnu.idatt2106.boco.models;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@Indexed
@Entity
@Table(name = "rating", uniqueConstraints = { @UniqueConstraint(columnNames = "ratingId") })
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ratingId")
    private Long ratingId;

    @Column(name = "rate")
    private double rate;

    @Column(name = "feedback")
    private String feedback;

    @ManyToOne
    @JoinColumn(name="rentalId")
    private Rental rental;

    @ManyToOne
    @JoinColumn(name="userId")
    private User user;


    public Rating(int rate, String feedback, Rental rental, User user){
        this.rate = rate;
        this.feedback = feedback;
        this.rental = rental;
        this.user = user;
    }

}