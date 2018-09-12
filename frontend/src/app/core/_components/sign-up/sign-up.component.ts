import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { first } from 'rxjs/operators';

import { User } from '../../_models/index';
import { AlertService, UserService } from '../../_services/index';
import { MainNavBarComponent } from '../main-nav-bar/main-nav-bar.component';
import { FileUploaderService } from './../../_services/file-uploader/file-uploader.service';


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
  fileToUpload: File;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private userService: UserService,
    private alertService: AlertService,
    private titleService: Title,
    private formBuilder: FormBuilder,
    private fileUploader: FileUploaderService) {
      this.titleService.setTitle(this.title);
  }

  ngOnInit() {
    // Use FormBuilder to create a form group
    this.signUpForm = this.formBuilder.group({
      firstname: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(45)]],
      lastname: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(45)]],
      email: ['', [Validators.required, Validators.maxLength(65)]],
      password: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(100)]],
      confirmPassword: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(100)]],
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
    if (this.signUpForm.invalid)
      return;

    console.debug("Going to create user!");
    // Create the user
    this.createUser(this.f.firstname.value, this.f.lastname.value, this.f.email.value, this.f.password.value, this.fileUploader.fileName);

    console.debug("Going to post the file!!");
    // Post the photo_prfil to the BackEnd-code.
    this.fileUploader.postFile()
  }


  createUser(firstname: string, lastname: string, email: string, password: string, picture: string): void {
    // Trim email whitespace
    email = email.trim();

    const user: User = {firstname, lastname, email, password, picture} as User;

    // Send user to server
    this.userService.create(user)
      .pipe(first())
      .subscribe(
        data => {
          this.data = data;
          this.alertService.success('Registration successful.', true);
          setTimeout(() => { this.router.navigate([this.returnUrl]); }, 2000);
        },
        error => {
          this.alertService.error(error.error.message);
        }
      );
  }


  onImageChanged($event) {
    this.fileUploader.onImageChanged($event)
  }

}
