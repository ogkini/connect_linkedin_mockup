import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { first } from 'rxjs/operators';

import { User } from '../../_models/index';
import { AlertService, UserService } from '../../_services/index';
import { MainNavBarComponent } from '../main-nav-bar/main-nav-bar.component';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.scss']
})
export class SignUpComponent implements OnInit {
  title = 'Sign Up to Connect';
  signUpForm: FormGroup;
  returnUrl: string;
  submitted = false;
  data: object;

  constructor(
      private route: ActivatedRoute,
      private router: Router,
      private userService: UserService,
      private alertService: AlertService,
      private titleService: Title,
      private formBuilder: FormBuilder) {
    this.titleService.setTitle(this.title);
  }

  ngOnInit() {
    // Use FormBuilder to create a form group
    this.signUpForm = this.formBuilder.group({
      firstname: ['', Validators.required],
      lastname: ['', Validators.required],
      email: ['', Validators.required],
      password: ['', Validators.required],
      confirmPassword: ['', Validators.required]
    });

    // Get return url from route parameters or default to '/sign-in'
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/sign-in';
  }

  // Convenience getter for easy access to form fields
  get f() { return this.signUpForm.controls; }

  // Submits the form
  onSubmit() {
    this.submitted = true;

    // If form is invalid stop here
    if (this.signUpForm.invalid) {
      return;
    }

    // Create the user
    this.createUser(this.f.firstname.value, this.f.lastname.value, this.f.email.value, this.f.password.value);
  }

  createUser(firstname: string, lastname: string, email: string, password: string): void {
    // Trim email whitespace
    email = email.trim();

    const user: User = {firstname, lastname, email, password} as User;

    // Send user to server
    this.userService.create(user)
      .pipe(
        first()
      )
      .subscribe(
        data => {
          this.data = data;
          this.alertService.success('Registration successful.', true);
          this.router.navigate([this.returnUrl]);
        },
        error => {
          this.alertService.error(error.error.message);
        }
      );
  }
}
