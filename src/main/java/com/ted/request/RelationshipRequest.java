package com.ted.request;

import javax.validation.constraints.NotNull;

public class RelationshipRequest {

    @NotNull
    private Long receiver;

    public Long getReceiver() {
        return receiver;
    }

    public void setReceiver(Long receiver) {
        this.receiver = receiver;
    }

}
