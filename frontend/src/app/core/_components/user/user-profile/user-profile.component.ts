import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { Title } from "@angular/platform-browser";
import { EducationService, ExperienceService, UserService, RelationshipService, UsersInteractionService } from "../../../_services";
import { AlertService, ConnectionConfigService } from "../../../_services";
import {Network, User} from "../../../_models";

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss']
})
export class UserProfileComponent implements OnInit {

  title: string;
  userId: number;
  profilePhotosEndpoint: string;
  user: User;
  network: Network;
  signedInUser: User;
  disabledButton = false;

  constructor(
    private titleService: Title,
    private route: ActivatedRoute,
    private userService: UserService,
    private experienceService: ExperienceService,
    private educationService: EducationService,
    private relationshipService: RelationshipService,
    private alertService: AlertService,
    private connConfig: ConnectionConfigService,
    private usersInteraction: UsersInteractionService
  ) {
    this.signedInUser = JSON.parse(localStorage.getItem('currentUser'));
    this.route.params.subscribe(params => {
      this.userId = +params['id'];
      // Here we don't forbid access to other users!
      this.getNetwork(this.userId);
      this.getUserById(this.userId);
    });
  }

  ngOnInit() {
  }

  private getUserById(id: number) {
    this.userService.getById(id)
      .subscribe(user => {
        this.user = user;

        this.titleService.setTitle(this.user.firstname + ' ' + this.user.lastname);
        this.profilePhotosEndpoint = this.connConfig.usersEndpoint;

        // Check if a new request can be sent
        this.disabledButton = this.user.relationshipBetween;
      }, error => {
        this.alertService.error(error.error.message);
        console.log(error);
      }
    );
  }

  private getNetwork(userId: number) {
    this.relationshipService.getAll(userId).subscribe(network => { this.network = network; });
  }

  sendFriendRequest() {
    this.usersInteraction.sendFriendRequest(this.userId);
    // Make Connect button disabled
    this.disabledButton = true;
  }

  sendMessage() {
    console.debug("Going to send a message..");
  }

}
