package com.ted.service;

import com.ted.exception.ResourceNotFoundException;
import com.ted.model.JobApply;
import com.ted.model.JobOffer;
import com.ted.model.Relationship;
import com.ted.model.User;
import com.ted.repository.JobOfferRepository;
import com.ted.repository.RelationshipRepository;
import com.ted.repository.UserRepository;
import com.ted.request.JobOfferRequest;
import com.ted.security.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class JobOfferService {

    @Autowired
    private JobOfferRepository jobOfferRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RelationshipRepository relationshipRepository;

    @Autowired
    private JobApplyService jobApplyService;

    @Autowired
    private ValidatePathService validatePathService;

    private static final Logger logger = LoggerFactory.getLogger(JobOfferService.class);

    // A user creates a jobOffer
    public JobOffer create(Long userId, JobOfferRequest jobOfferRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        JobOffer jobOffer = new JobOffer();

        jobOffer.setOwner(user);
        jobOffer.setText(jobOfferRequest.getText());

        return jobOfferRepository.save(jobOffer);
    }

    // Returns a user's jobOffers
    public List<JobOffer> getAll(Long userId, UserDetailsImpl currentUser) {
        List<JobOffer> jobOffers = jobOfferRepository.getAllByUserId(userId);

        for (JobOffer jo : jobOffers) {
            // Check the jobOffers that the current user has jobApply
            for (JobApply ja : jo.getJobApplies()) {
                jo.setAppliedToJobOffer(currentUser.getId() == ja.getUser().getId());
            }

            // Set the jobApplies counter
            jo.setJobAppliesCount(jobApplyService.getJobAppliesCount(jo.getId()));
        }

        System.out.println(jobOffers.size());

        return jobOffers;
    }

    // Returns a user's network's jobOffers (including his own jobOffers)
    public List<JobOffer> getNetworkJobOffers(Long userId, UserDetailsImpl currentUser) {
        List<JobOffer> jobOffers = new ArrayList<>();

        // Get the user's network
        List<Relationship> connections = relationshipRepository.getConnectionsByUserId(userId);

        // Get the jobOffers of the specific users and add them to the list
        for (Relationship c : connections) {
            if (userId == c.getSender().getId()) {
                jobOffers.addAll(getAll(c.getReceiver().getId(), currentUser));
            } else {
                jobOffers.addAll(getAll(c.getSender().getId(), currentUser));
            }
        }

        // Add the users own jobOffers to the list
        jobOffers.addAll(getAll(userId, currentUser));

        // Sort the jobOffers from newest to oldest
        Collections.sort(jobOffers, new Comparator<JobOffer>(){
            public int compare(JobOffer p1, JobOffer p2) {
                return p2.getCreatedTime().compareTo(p1.getCreatedTime());
            }
        });

        return jobOffers;
    }

}
