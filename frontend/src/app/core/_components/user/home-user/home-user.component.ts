import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';
import { ActivatedRoute } from "@angular/router";

import { User, Experience, Education, CreationResponse } from '../../../_models/index';
import { UserService, ExperienceService, EducationService } from '../../../_services/index';
import { AlertService, ConnectionConfigService, DataService } from '../../../_services/index';
import { UserNavBarComponent } from './../user-nav-bar/user-nav-bar.component';
import { DateService } from "../../../_services/date.service";
import { DatePeriodValidatorDirective } from "../../../_directives/validators/date-period-validator.directive";

@Component({
  selector: 'app-home-user',
  templateUrl: './home-user.component.html',
  styleUrls: ['./home-user.component.scss']
})
export class HomeUserComponent implements OnInit {

  title = 'Home';
  currentUser: User;
  user: User = {} as User;
  submitted = false;
  message: string;

  public profilePhotosEndpoint: string;

  public constructor(
      private titleService: Title,
      private userService: UserService,
      private experienceService: ExperienceService,
      private educationService: EducationService,
      private alertService: AlertService,
      private formBuilder: FormBuilder,
      private connConfig: ConnectionConfigService,
      private dataService: DataService
  ) {
    this.titleService.setTitle(this.title);
    this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
    this.profilePhotosEndpoint = this.connConfig.serverUrl + this.connConfig.userFilesEndpoint;
  }

  ngOnInit() {
    this.getUserById(this.currentUser.id);
    this.dataService.currentMessage.subscribe(message => this.message = message);
  }

  private getUserById(id: number) {
    this.userService.getById(id).subscribe( user => {
      this.user = user;
      this.dataService.changeMessage(this.user.newFriendRequests.toString());
    });
  }

  clearAlert() {
    this.alertService.clear();
  }

}
