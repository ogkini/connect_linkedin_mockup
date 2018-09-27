import { Component, OnInit } from '@angular/core';
import {Title} from "@angular/platform-browser";
import {
  AlertService,
  ConnectionConfigService,
  UserService, AuthenticationService
} from "../../../_services";
import {FormBuilder} from "@angular/forms";
import {User} from "../../../_models";
import {FormGroup, Validators} from "@angular/forms";
import {TextValidatorDirective} from "../../../_directives/validators/text_validator.directive";
import {PasswordConfirmValidatorDirective} from "../../../_directives/validators/password-confirm-validator.directive";
import {ActivatedRoute, Router} from "@angular/router";
import {first} from "rxjs/operators";


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
    private authenticationService: AuthenticationService
  ) {
    this.titleService.setTitle(this.title);
    this.signedInUser = JSON.parse(localStorage.getItem('currentUser'));
    this.route.params.subscribe(params => {
      this.userId = +params['id'];
      this.authenticationService.forbidUnauthorizedAccess(this.signedInUser, this.userId);
      this.getUserById(this.userId);
    });
  }

  minTextLength = 2;
  maxTextLength = 45;
  maxEmailLength = 65;
  minPasswordLength = 6;
  maxPasswordLength = 100;
  
  // Todo - make a form-service, which will provide the arrays of the validators for each form-field.
  ngOnInit() {
    // Use FormBuilder to create a form group
    this.updateForm = this.formBuilder.group({
      firstname: ['', [Validators.required, TextValidatorDirective.validateCharacters, Validators.minLength(this.minTextLength), Validators.maxLength(this.maxTextLength)]],
      lastname: ['', [Validators.required, TextValidatorDirective.validateCharacters, Validators.minLength(this.minTextLength), Validators.maxLength(this.maxTextLength)]],
      occupation: ['', [Validators.required, Validators.minLength(this.minTextLength), Validators.maxLength(this.maxTextLength)]],
      email: ['', [Validators.required, Validators.email, Validators.maxLength(this.maxEmailLength)]],
      password: ['', [Validators.required, Validators.minLength(this.minPasswordLength), Validators.maxLength(this.maxPasswordLength)]],
      confirmNewPassword: ['', [PasswordConfirmValidatorDirective.validatePasswordConfirmation]]
    });
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
      return;
    }

    // Create the user
    this.updateUser(this.form.firstname.value, this.form.lastname.value, this.form.email.value, this.form.password.value);

    // Go to user's-homePage.
    if ( !this.router.navigate(['/users', this.signedInUser.id + '/home']) ) {
      console.error("Navigation from \"SignIn\" to \"/users/\"" + this.signedInUser.id + "\" failed!");
    }
  }

  updateUser(firstname: String, lastname: string, email: string, password: string) {

    // "picture" will be

    // TODO - Make html-default"value"s to count as input.

    // Trim email whitespace
    email = email.trim();

    const user: User = {firstname, lastname, email, password} as User;

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
