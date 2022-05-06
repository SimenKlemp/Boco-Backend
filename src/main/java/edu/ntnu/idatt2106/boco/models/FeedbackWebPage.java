package edu.ntnu.idatt2106.boco.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "feedback",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "feedbackId")
        })

public class FeedbackWebPage
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedbackId")
    private Long feedbackId;


    @Column(name = "message", length = 200)
    private String message;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    public FeedbackWebPage(String message, User user)
    {
        this.message = message;
        this.user = user;
    }
}




