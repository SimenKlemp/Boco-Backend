package edu.ntnu.idatt2106.boco.models;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@Indexed
@SuperBuilder
@Entity
@Table(name = "notification", uniqueConstraints = { @UniqueConstraint(columnNames = "notificationId") })
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notificationId")
    private Long notificationId;

    @NotBlank
    @Column(name = "notificationStatus")
    private String notificationStatus;

    @Column(name = "isPressed")
    private boolean isPressed;

    @ManyToOne
    @JoinColumn(name="rentalId")
    private Rental rental;

    @ManyToOne
    @JoinColumn(name="userId")
    private User user;


    public Notification(String notificationStatus, boolean isPressed, Rental rental, User user){
        this.notificationStatus = notificationStatus;
        this.isPressed = isPressed;
        this.rental = rental;
        this.user = user;

    }

}

