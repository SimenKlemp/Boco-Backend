package edu.ntnu.idatt2106.boco.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Entity
@Table(name = "feedback",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "feedbackId")
        })

public class FeedbackWebPage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedbackId")
    private Long feedbackId;

    @NotBlank
    @Size(max = 20)
    @Column(name = "feedbackMessage")
    private String streetAddress;

    @ManyToOne
    @JoinColumn(name="userId")
    private User user;

    public FeedbackWebPage(){

    }

    public FeedbackWebPage(String streetAddress, User user) {
        this.streetAddress = streetAddress;
        this.user = user;
    }

    public Long getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}



