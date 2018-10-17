import { Component, OnInit } from '@angular/core';
import {CreationResponse, JobOffer, JobApply, User} from "../../../_models";
import {FormBuilder, FormGroup} from "@angular/forms";
import {Title} from "@angular/platform-browser";
import {
  AuthenticationService,
  CommentService,
  ConnectionConfigService
} from "../../../_services";
import {ActivatedRoute} from "@angular/router";
import {first} from "rxjs/operators";
import {JobOfferService} from "../../../_services/job-offer.service";
import {JobApplyService} from "../../../_services/job-apply.service";

@Component({
  selector: 'app-job-offers',
  templateUrl: './job-offers.component.html',
  styleUrls: ['./job-offers.component.scss']
})
export class JobOffersComponent implements OnInit {

  title = 'Job offers';
  signedInUser: User;
  jobOffers: JobOffer[] = [];
  submitted = false;
  userId: number;
  showApplies = false;
  public jobOfferToSeeAppliesFrom: JobOffer;

  addJobOfferForm: FormGroup;

  public profilePhotosEndpoint: string;

  public constructor(
    private titleService: Title,
    private jobOfferService: JobOfferService,
    private jobApplyService: JobApplyService,
    private commentService: CommentService,
    private formBuilder: FormBuilder,
    private connConfig: ConnectionConfigService,
    private route: ActivatedRoute,
    private authenticationService: AuthenticationService
  ) {
    this.titleService.setTitle(this.title);
    this.signedInUser = JSON.parse(localStorage.getItem('currentUser'));
    this.route.params.subscribe(params => {
      this.userId = +params['id'];
      this.authenticationService.forbidUnauthorizedAccess(this.signedInUser, this.userId);
      this.getTimelineJobOffers(this.userId);
    });
    this.profilePhotosEndpoint = this.connConfig.usersEndpoint;
  }

  ngOnInit() {
    // Initialise form contents
    this.addJobOfferFormInit();
  }

  private getTimelineJobOffers(id: number) {
    this.jobOfferService.getTimeline(id).subscribe(jobOffers => { this.jobOffers = jobOffers; });
  }

  // Initiliases the form to create a new jobOffer
  addJobOfferFormInit() {
    this.addJobOfferForm = this.formBuilder.group({
      text: ['']
    });
  }

  // Convenience getters for easy access to form fields
  get getAddJobOfferForm() { return this.addJobOfferForm.controls; }

  // A user creates a jobOffer
  addJobOffer() {
    // Create a new JobOffer object
    const newJobOffer: JobOffer = {
      text: this.getAddJobOfferForm.text.value
    } as JobOffer;

    // Submit the jobOffer to the server
    this.jobOfferService.create(newJobOffer, this.signedInUser.id)
      .pipe(first())
      .subscribe((response: CreationResponse) => {
          // Add the jobOffer to the beginning of the array
          if (response.object) {
            this.jobOffers.unshift(response.object);
          }

          // Clear the form
          this.addJobOfferFormInit();
        }, error => {
          console.error(error);
        }
      );
  }

  // A user applies to a jobOffer
  apply(ownerId: number, jobOfferId: number) {
    this.jobApplyService.create(ownerId, jobOfferId)
      .pipe(first())
      .subscribe((response: CreationResponse) => {
          // Add application to the array
          if (response.object) {
            this.jobOffers.find(jobOffer => jobOffer.id == jobOfferId).jobApplies.push(response.object);
          }

          // Update boolean to indicate that the user applies to the jobOffer
          this.jobOffers.find(jobOffer => jobOffer.id == jobOfferId).appliedToJobOffer = true;

          // Update applies counter
          this.jobOffers.find(jobOffer => jobOffer.id == jobOfferId).jobAppliesCount++;
          
        }, error => {
          console.error(error);
        }
      );
  }

  // A user un-applies
  unApply(ownerId: number, jobOfferId: number) {
    // Find the id of the jobApply to be un-done.
    let jobApplyId = this.jobOffers.find(jobOffer => jobOffer.id == jobOfferId).jobApplies.find(jobApply => jobApply.user.id == this.signedInUser.id).id;

    this.jobApplyService.delete(ownerId, jobOfferId, jobApplyId)
      .pipe(first())
      .subscribe(response => {

          for ( let jobOffer of this.jobOffers )
          {
            if ( jobOffer.id == jobOfferId ) {
              console.debug("Found jobOffer with Id: " + jobOfferId);
              jobOffer.appliedToJobOffer = false;
              jobOffer.jobAppliesCount--;
            }
          }

          // Remove the apply from the array
          this.jobOffers.find(jobOffer => jobOffer.id == jobOfferId).jobApplies = this.jobOffers.find(jobOffer => jobOffer.id == jobOfferId)
            .jobApplies.filter(jobApply => jobApply.user.id !== this.signedInUser.id);

         /* // Update boolean to indicate that the user applies to the jobOffer
          this.jobOffers.find(jobOffer => jobOffer.id == jobOfferId).appliedToJobOffer = false;

          // Update applies counter
          this.jobOffers.find(jobOffer => jobOffer.id == jobOfferId).jobAppliesCount --;*/
        }, error => {
          console.error(error);
        }
      );
  }

  showAppliesToggle(jobOffer: JobOffer) {
    if ( this.jobOfferToSeeAppliesFrom == undefined)
      this.jobOfferToSeeAppliesFrom = jobOffer;
    else
      this.jobOfferToSeeAppliesFrom = undefined;
  }

}
