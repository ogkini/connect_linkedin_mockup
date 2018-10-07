import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { first } from 'rxjs/operators';

import { User } from '../../_models';
import {AlertService, AuthenticationService, UserService, FileUploaderService, ValidatorService} from '../../_services';


@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.scss']
})
export class SignUpComponent implements OnInit {

  title = 'Sign Up to Connect';
  signUpForm: FormGroup;
  submitted = false;
  data: object;
  user: User;

  constructor(
      private route: ActivatedRoute,
      private router: Router,
      private userService: UserService,
      private alertService: AlertService,
      private titleService: Title,
      private formBuilder: FormBuilder,
      private authenticationService: AuthenticationService,
      private fileUploader: FileUploaderService,
      private formService: ValidatorService
  ) {
    this.titleService.setTitle(this.title);
  }

  ngOnInit() {
    // Use FormBuilder to create a form group
    this.signUpForm = this.formBuilder.group({
      firstname: ['', this.formService.getFirstnameValidatorsArray()],
      lastname: ['', this.formService.getLastnameValidatorsArray()],
      email: ['', this.formService.getEmailValidatorsArray()],
      password: ['', this.formService.getPasswordValidatorsArray()],
      confirmPassword: ['', this.formService.getConfirmPasswordValidatorsArray()]
    });
  }

  // Convenience getter for easy access to form fields
  get form() { return this.signUpForm.controls; }

  // Submits the form
  onSubmit() {
    this.submitted = true;

    // If form is invalid stop here
    if (this.signUpForm.invalid) {
      return;
    }

    // Create the user
    this.createUser(this.form.firstname.value, this.form.lastname.value, this.form.email.value, this.form.password.value, this.fileUploader.fileName);
  }

  createUser(firstname: string, lastname: string, email: string, password: string, picture: string) {
    // Trim email whitespace
    email = email.trim();

    const user: User = {firstname, lastname, email, password, picture} as User;

    // Send user to server
    this.userService.create(user)
      .pipe(first())
      .subscribe(
        data => {
          //this.data = data; // Unused..
          this.alertService.success('Registration successful.', true);
          if ( this.fileUploader.fileToUpload != null )
          {
            this.fileUploader.postFile(email).subscribe( // Send photo to backend
              response => {
                console.log("Response: " + response);
                this.authenticationService.authenticateUserAndGoToHome(email, password);
              }
            );
          }
          else
            this.authenticationService.authenticateUserAndGoToHome(email, password);
        },
        error => {
          this.alertService.error(error.error.message);
        }
      );
  }

  onImageChange($event) {
    console.debug("Inside \"onImageChange()\"!");
    this.fileUploader.onImageChange($event)
  }

  onImageReset() {
    console.debug("Going to reset the \"fileToUpload\".");
    this.fileUploader.onFileReset();
  }

}
