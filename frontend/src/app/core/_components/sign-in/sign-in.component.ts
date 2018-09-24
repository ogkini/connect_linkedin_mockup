import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { first } from 'rxjs/operators';

import { AlertService, AuthenticationService } from '../../_services/index';
import { MainNavBarComponent } from '../main-nav-bar/main-nav-bar.component';
import { User } from "../../_models";

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.scss']
})
export class SignInComponent implements OnInit {

  title = 'Sign In to Connect';
  currentUser: User;
  signInForm: FormGroup;
  submitted = false;
  returnUrl: string;
  data: HttpResponse<{}>;

  public constructor(
    private route: ActivatedRoute,
    private router: Router,
    private alertService: AlertService,
    private authenticationService: AuthenticationService,
    private titleService: Title,
    private formBuilder: FormBuilder
  ) {
    this.titleService.setTitle(this.title);
  }

  maxEmailLength = 65;
  minPasswordLength = 6;
  maxPasswordLength = 100;

  ngOnInit() {
    // Use FormBuilder to create a form group
    this.signInForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email, Validators.maxLength(this.maxEmailLength)]],
      password: ['', [Validators.required, Validators.minLength(this.minPasswordLength), Validators.maxLength(this.maxPasswordLength)]]
    });

    // Reset login status
    this.authenticationService.logout();
  }

  // Convenience getter for easy access to form fields
  get form() {
    return this.signInForm.controls;
  }

  // Submits the form
  onSubmit() {
    this.submitted = true;

    // If form is invalid stop here
    if (this.signInForm.invalid) {
      return;
    }

    console.debug("Going to authenticate user with email:", this.form.email.value);

    this.authenticationService.authenticateUserAndGoToHome(this.form.email.value, this.form.password.value);
  }

}
