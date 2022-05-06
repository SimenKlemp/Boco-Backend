package edu.ntnu.idatt2106.boco.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "rental")
public class Rental
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rentalId")
    private Long rentalId;

    @Column(name = "startDate")
    private Date startDate;

    @Column(name = "endDate")
    private Date endDate;

    @Column(name = "status")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @JoinColumn(name = "itemId")
    @ManyToOne
    private Item item;

    @Column(name = "deliveryInfo")
    private DeliverInfo deliveryInfo;

    @JoinColumn(name = "rentalId")
    @OneToMany
    private List<Message> messages;

    public Rental(Date startDate, Date endDate, Status status, User user, Item item, DeliverInfo deliveryInfo)
    {
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.user = user;
        this.item = item;
        this.deliveryInfo = deliveryInfo;
    }

    public enum Status
    {
        PENDING,
        ACCEPTED,
        REJECTED,
        CANCELED
    }

    public enum DeliverInfo
    {
        PICKUP,
        DELIVERED
    }
}
