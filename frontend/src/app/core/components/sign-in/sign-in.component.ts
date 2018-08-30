import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { MainNavBarComponent } from './../main-nav-bar/main-nav-bar.component';

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.scss']
})
export class SignInComponent implements OnInit {

  title = 'Sign In to Connect';

  signInForm: FormGroup;

  public constructor(private titleService: Title, private formBuilder: FormBuilder) {
    this.titleService.setTitle( this.title );
  }

  ngOnInit() {
    // Use FormBuilder to create a form group
    this.signInForm = this.formBuilder.group({
      email: ['', Validators.required],
      password: ['', Validators.required],
    });
  }

  // Submits the form
  onSubmit() {}

}
