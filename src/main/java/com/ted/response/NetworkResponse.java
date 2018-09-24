package com.ted.response;

import com.ted.model.User;
import com.ted.model.Relationship;

import java.util.*;

public class NetworkResponse {

    List<User> connections = new ArrayList<>();
    List<Relationship> receivedRequests = new ArrayList<>();
    List<Relationship> sentRequests = new ArrayList<>();

    public NetworkResponse() {}

    public NetworkResponse(List<User> connections, List<Relationship> received, List<Relationship> sent) {
        this.connections = connections;
        this.receivedRequests = received;
        this.sentRequests = sent;
    }

    public List<User> getConnections() {
        return connections;
    }

    public void setConnections(List<User> connections) {
        this.connections = connections;
    }

    public List<Relationship> getReceivedRequests() {
        return receivedRequests;
    }

    public void setReceivedRequests(List<Relationship> receivedRequests) {
        this.receivedRequests = receivedRequests;
    }

    public List<Relationship> getSentRequests() {
        return sentRequests;
    }

    public void setSentRequests(List<Relationship> sentRequests) {
        this.sentRequests = sentRequests;
    }

}
