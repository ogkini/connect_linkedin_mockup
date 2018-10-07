import { Component, OnInit } from '@angular/core';
import { Title } from "@angular/platform-browser";
import { FormGroup, FormBuilder } from "@angular/forms";
import { ActivatedRoute, Router } from "@angular/router";
import { first } from "rxjs/operators";

import { User } from "../../../_models";
import { AlertService, ConnectionConfigService, UserService, AuthenticationService, ValidatorService } from "../../../_services";

@Component({
  selector: 'app-user-settings',
  templateUrl: './user-settings.component.html',
  styleUrls: ['./user-settings.component.scss']
})
export class UserSettingsComponent implements OnInit {

  private title: string = 'Settings';
  updateForm: FormGroup;
  submitted = false;
  data: object;
  public signedInUser: User;
  public user: User;
  public userId: number;

  public constructor(
      private titleService: Title,
      private userService: UserService,
      private alertService: AlertService,
      private formBuilder: FormBuilder,
      private connConfig: ConnectionConfigService,
      private route: ActivatedRoute,
      private router: Router,
      private authenticationService: AuthenticationService,
      private formService: ValidatorService
  ) {
    this.titleService.setTitle(this.title);
    this.signedInUser = JSON.parse(localStorage.getItem('currentUser'));
    this.route.params.subscribe(params => {
      this.userId = +params['id'];
      this.authenticationService.forbidUnauthorizedAccess(this.signedInUser, this.userId);
      this.getUserById(this.userId);
    });

    this.updateForm = this.formBuilder.group({
      firstname: ['', this.formService.getFirstnameValidatorsArray()],
      lastname: ['', this.formService.getLastnameValidatorsArray()],
      email: ['', this.formService.getEmailValidatorsArray()],
      password: ['', this.formService.getPasswordValidatorsArray()],
      confirmNewPassword: ['', this.formService.getConfirmPasswordValidatorsArray()]
    });
  }

  ngOnInit() {
  }

  private getUserById(id: number) {
    this.userService.getById(id).subscribe(user => {
      this.user = user;
    },error => {
       this.alertService.error(error.error.message);
       console.log(error);
      }
    );
  }

  // Convenience getter for easy access to form fields
  get form() { return this.updateForm.controls; }

  // Submits the form
  onSubmit() {
    this.submitted = true;

    // If form is invalid stop here
    if ( this.updateForm.invalid ) {
      console.warn("UpdateForm is invalid!");
      return;
    }

    // Create the user
    this.updateUser(this.userId, this.form.firstname.value, this.form.lastname.value, this.form.email.value, this.form.password.value, this.user.picture);

    this.userService.setIsChangedAccountData(true);

    // Go to user's-homePage.
    if (!this.router.navigate(['/users', this.signedInUser.id])) {
      console.error("Navigation from \"Settings\" to \"/users/" + this.signedInUser.id + "/home\" failed!");
    }
  }

  updateUser(id: number, firstname: String, lastname: string, email: string, password: string, picture: string) {

    // Trim email whitespace
    email = email.trim();

    const user: User = {id, firstname, lastname, email, password, picture} as User;

    // Send user to server
    this.userService.update(user)
      .pipe(first())
      .subscribe(
        data => {
          this.data = data;
          this.alertService.success("User's update successful.", true);
        },
        error => {
          this.alertService.error(error.error.message);
        }
      );
  }

}
