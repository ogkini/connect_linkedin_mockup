import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { first } from 'rxjs/operators';

import { AlertService, AuthenticationService } from '../../_services/index';
import { MainNavBarComponent } from '../main-nav-bar/main-nav-bar.component';

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.scss']
})
export class SignInComponent implements OnInit {
  title = 'Sign In to Connect';
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
      private formBuilder: FormBuilder) {
    this.titleService.setTitle(this.title);
  }

  ngOnInit() {
    // Use FormBuilder to create a form group
    this.signInForm = this.formBuilder.group({
      email: ['', Validators.required],
      password: ['', Validators.required],
    });

    // Reset login status
    this.authenticationService.logout();
  }

  // Convenience getter for easy access to form fields
  get f() { return this.signInForm.controls; }

  // Submits the form
  onSubmit() {
    this.submitted = true;

    // Set url to redirect to after signin
    if (this.f.email.value === 'admin@mail.com') {
      this.returnUrl = '/welcome-admin';
    } else {
      this.returnUrl = '/welcome-user';
    }

    this.authenticationService.login(this.f.email.value, this.f.password.value)
      .pipe(first())
      .subscribe(
        data => {
          this.router.navigate([this.returnUrl]);
        },
        error => {
          this.alertService.error(error.error.message);
      });
  }
}
