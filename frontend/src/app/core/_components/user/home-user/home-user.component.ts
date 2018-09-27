import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { FormBuilder } from '@angular/forms';

import { User } from '../../../_models';
import {
  UserService,
  ExperienceService,
  EducationService,
  AlertService,
  ConnectionConfigService,
  DataService,
  AuthenticationService
} from '../../../_services';
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-home-user',
  templateUrl: './home-user.component.html',
  styleUrls: ['./home-user.component.scss']
})
export class HomeUserComponent implements OnInit {

  title = 'Home';
  signedInUser: User;
  user: User = {} as User;
  submitted = false;
  message: string;
  userId: number;

  public profilePhotosEndpoint: string;

  public constructor(
      private titleService: Title,
      private userService: UserService,
      private experienceService: ExperienceService,
      private educationService: EducationService,
      private alertService: AlertService,
      private formBuilder: FormBuilder,
      private connConfig: ConnectionConfigService,
      private dataService: DataService,
      private route: ActivatedRoute,
      private authenticationService: AuthenticationService,
  ) {
    this.titleService.setTitle(this.title);
    this.signedInUser = JSON.parse(localStorage.getItem('currentUser'));
    this.route.params.subscribe(params => {
      this.userId = +params['id'];
      this.authenticationService.forbidUnauthorizedAccess(this.signedInUser, this.userId);
      this.getUserById(this.userId);
    });
  }

  ngOnInit() {
    this.dataService.currentMessage.subscribe(message => this.message = message);
  }

  private getUserById(id: number) {
    this.userService.getById(id).subscribe( user => {
      this.user = user;
      this.dataService.changeMessage(this.user.newFriendRequests.toString());
      this.profilePhotosEndpoint = this.connConfig.usersEndpoint + '/' + this.user.id + '/photos';
    });
  }

  clearAlert() {
    this.alertService.clear();
  }

}
