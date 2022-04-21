package edu.ntnu.idatt2106.boco.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "rental",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "itemId")
        })

public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rentalId")
    private Long rentalId;

    @NotBlank
    @Size(max = 20)
    @Column(name = "message")
    private String message;

    @NotBlank
    @Column(name = "startDate")
    private Date startDate;

    @NotBlank
    @Column(name = "endDate")
    private Date endDate;

    @NotBlank
    @Size(max = 20)
    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name="userId")
    private User user;

    @Column(name = "itemId")
    private Long itemId;

    public Rental(){

    }

    public Rental(String message, Date startDate, Date endDate, String status, User user, Long itemId){
        this.message = message;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.user = user;
        this.itemId = itemId;
    }

    public Long getRentalId() {
        return rentalId;
    }

    public void setRentalId(Long rentalId) {
        this.rentalId = rentalId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }
}
