import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { Title } from "@angular/platform-browser";
import { EducationService, ExperienceService, UserService, RelationshipService } from "../../../_services";
import { AlertService, ConnectionConfigService } from "../../../_services";
import { FormBuilder, FormGroup, Validators} from "@angular/forms";
import { CreationResponse, User, FriendRequest } from "../../../_models";
import { DatePeriodValidatorDirective } from "../../../_directives/validators/date-period-validator.directive";
import { DateService } from "../../../_services/date.service";
import { first } from "rxjs/operators";

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
    private formBuilder: FormBuilder,
    private connConfig: ConnectionConfigService,
  ) {
    this.signedInUser = JSON.parse(localStorage.getItem('currentUser'));
    this.route.params.subscribe(params => {
      this.userId = +params['id'];
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

  sendFriendRequest() {
    // Create a new Friend Request object
    const newFriendRequest: FriendRequest = {
      receiver: this.userId,
    } as FriendRequest;

    // Submit the experience to the server
    this.relationshipService.create(newFriendRequest)
      .pipe(first())
      .subscribe((response: CreationResponse) => {
        // Make Connect button disabled
        this.disabledButton = true;
      }, error => {
        console.log(error.error);
      }
      );
  }

  sendMessage() {
    console.debug("Going to send a message..");
  }

}
