package com.ted.request;

import javax.validation.constraints.NotNull;

public class ConversationRequest {

    @NotNull
    private Long receiver;

    public ConversationRequest() {}

    public ConversationRequest(Long receiver) {
        this.receiver = receiver;
    }

    public Long getReceiver() {
        return receiver;
    }

    public void setReceiver(Long receiver) {
        this.receiver = receiver;
    }

}
