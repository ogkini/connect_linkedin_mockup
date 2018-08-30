import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { MainNavBarComponent } from './../main-nav-bar/main-nav-bar.component';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.scss']
})
export class SignUpComponent implements OnInit {

  title = 'Sign Up to Connect';

  signUpForm: FormGroup;

  constructor(private titleService: Title, private formBuilder: FormBuilder) {
    this.titleService.setTitle( this.title );
  }

  ngOnInit() {
    // Use FormBuilder to create a form group
    this.signUpForm = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', Validators.required],
      password: ['', Validators.required],
      confirmPassword: ['', Validators.required]
    });
  }

  // Submits the form
  onSubmit() {}

}
