package edu.ntnu.idatt2106.boco.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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


    @Size(max = 20)
    @Column(name = "message")
    private String message;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    public FeedbackWebPage(String message, User user) {
        this.message = message;
        this.user = user;
    }
}




