package com.ijaa.user.domain.request;

import jakarta.validation.constraints.NotBlank;

public class InterestRequest {
    @NotBlank(message = "Interest must not be blank")
    private String interest;

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }
} 