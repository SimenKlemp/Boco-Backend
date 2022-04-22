package edu.ntnu.idatt2106.boco.payload.request;

import edu.ntnu.idatt2106.boco.models.User;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

public class FeedbackWebPageRequest {
    @NotBlank
    private String feedbackMessage;

    @NotBlank
    private Long userId;

    public FeedbackWebPageRequest(String feedbackMessage, Long userId) {
        this.feedbackMessage = feedbackMessage;
        this.userId = userId;
    }

    public String getFeedbackMessage() {
        return feedbackMessage;
    }

    public void setFeedbackMessage(String feedbackMessage) {
        this.feedbackMessage = feedbackMessage;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
