import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';
import { ActivatedRoute } from "@angular/router";

import { User, Experience, Education, CreationResponse } from '../../../_models/index';
import { UserService, ExperienceService, EducationService } from '../../../_services/index';
import { AlertService, ConnectionConfigService } from '../../../_services/index';
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
  user: User;
  submitted = false;
  public profilePhotosEndpoint: string;
  //public userId: number;

  public constructor(
      private titleService: Title,
      private userService: UserService,
      private experienceService: ExperienceService,
      private educationService: EducationService,
      private alertService: AlertService,
      private formBuilder: FormBuilder,
      private connConfig: ConnectionConfigService,
      //private route: ActivatedRoute
  ) {
    this.titleService.setTitle(this.title);
    this.currentUser = JSON.parse(localStorage.getItem('currentUser'));

    // this.route.params.subscribe( params => {
    //   this.userId = +params['id']; // (+) converts string 'id' to a number
    //   console.debug("UserID coming from url-parameters is:", this.userId);
    // });
    //
    this.profilePhotosEndpoint = this.connConfig.serverUrl + this.connConfig.userFilesEndpoint;
  }

  ngOnInit() {
    this.getUserById(this.currentUser.id);
  }

  private getUserById(id: number) {
    this.userService.getById(id).subscribe(user => { this.user = user; });
      // .subscribe((response: any) => {
      //     this.user = response.content;
      //   }, error => {
      //     this.alertService.error(error.error.message);
      //     console.log(error);
      //   }
      // );
  }

  clearAlert() {
    this.alertService.clear();
  }

}
