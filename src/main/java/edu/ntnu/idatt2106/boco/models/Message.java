package edu.ntnu.idatt2106.boco.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "message")
public class Message
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "messageId")
    private Long messageId;

    @Column(name = "text")
    private String text;

    @Column(name = "isByUser")
    private Boolean isByUser;

    @Column(name = "date")
    private Date date;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "rentalId")
    private Rental rental;

    public Message(String text, Boolean isByUser, Date date, User user, Rental rental)
    {
        this.text = text;
        this.isByUser = isByUser;
        this.date = date;
        this.user = user;
        this.rental = rental;
    }
}
