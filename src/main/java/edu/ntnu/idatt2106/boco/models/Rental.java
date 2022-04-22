package edu.ntnu.idatt2106.boco.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "rental", uniqueConstraints = { @UniqueConstraint(columnNames = "itemId") })
public class Rental
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rentalId")
    private Long rentalId;

    @NotBlank
    @Size(max = 20)
    @Column(name = "message")
    private String message;

    @Column(name = "startDate")
    private Date startDate;

    @Column(name = "endDate")
    private Date endDate;

    @NotBlank
    @Size(max = 20)
    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name="userId")
    private User user;

    @JoinColumn(name = "itemId")
    @ManyToOne
    private Item item;

    public Rental(String message, Date startDate, Date endDate, String status, User user, Item item)
    {
        this.message = message;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.user = user;
        this.item = item;
    }
}
