import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import {Title} from "@angular/platform-browser";
import {
  AlertService,
  ConnectionConfigService,
  UserService
} from "../../../_services";
import {FormBuilder} from "@angular/forms";
import {User} from "../../../_models";
import {FormGroup, Validators} from "@angular/forms";
import {TextValidatorDirective} from "../../../_directives/validators/text_validator.directive";
import {PasswordConfirmValidatorDirective} from "../../../_directives/validators/password-confirm-validator.directive";
import {ActivatedRoute, Router} from "@angular/router";
import {FileUploaderService} from "../../../_services/file-uploader/file-uploader.service";
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
  fileToUpload: File;
  public currentUser: User;
  public user: User;
  public profilePhotosEndpoint: string;

  public constructor(
    private titleService: Title,
    private userService: UserService,
    private alertService: AlertService,
    private formBuilder: FormBuilder,
    private connConfig: ConnectionConfigService,
    private route: ActivatedRoute,
    private router: Router,
    private fileUploader: FileUploaderService,
    private location: Location
  ) {
    this.titleService.setTitle(this.title);
    this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
    this.profilePhotosEndpoint = this.connConfig.usersEndpoint + '/' + this.currentUser.id + '/photos';
  }

  minTextLength = 2;
  maxTextLength = 45;

  maxEmailLength = 65;

  minPasswordLength = 6;
  maxPasswordLength = 100;
  
  // Todo - make a form-service, which will provide the arrays of the validators for each form-field.
  ngOnInit() {
    this.getUserById(this.currentUser.id);

    // Use FormBuilder to create a form group
    this.updateForm = this.formBuilder.group({
      firstname: ['', [Validators.required, TextValidatorDirective.validateCharacters, Validators.minLength(this.minTextLength), Validators.maxLength(this.maxTextLength)]],
      lastname: ['', [Validators.required, TextValidatorDirective.validateCharacters, Validators.minLength(this.minTextLength), Validators.maxLength(this.maxTextLength)]],
      occupation: ['', [Validators.required, Validators.minLength(this.minTextLength), Validators.maxLength(this.maxTextLength)]],
      email: ['', [Validators.required, Validators.email, Validators.maxLength(this.maxEmailLength)]],
      password: ['', [Validators.required, Validators.minLength(this.minPasswordLength), Validators.maxLength(this.maxPasswordLength)]],
      confirmNewPassword: ['', [PasswordConfirmValidatorDirective.validatePasswordConfirmation]]
    });

    // Get return url from route parameters or default to '/sign-in'
  }

  private getUserById(id: number) {
    this.userService.getById(id).subscribe(user => { this.user = user; });
    // .subscribe((response: any) => {
    //     this.user = response.content;
    //   }, error => {
    //     this.alertService.error(error.message);
    //     console.log(error);
    //   }
    // );
  }

  // Convenience getter for easy access to form fields
  get form() { return this.updateForm.controls; }

  // Submits the form
  onSubmit() {
    console.debug("Inside \"onSubmit()\"");

    this.submitted = true;

    // If form is invalid stop here
    if (this.updateForm.invalid) {
      return;
    }

    // Create the user
    this.updateUser(this.form.firstname.value, this.form.lastname.value, this.form.email.value, this.form.password.value, this.fileUploader.fileName);

    if ( !this.router.navigate(['/users', this.currentUser.id]) ) {
      console.error("Navigation from \"SignIn\" to \"/users/\"" + this.currentUser.id + "\" failed!");
    }
  }

  updateUser(firstname: String, lastname: string, email: string, password: string, picture: string) {

    // TODO - Investigate case where "picture" is not present.. This should be done also for the rest of the data.

    // TODO - Make html-default"value"s to count as input.

    // Trim email whitespace
    email = email.trim();

    const user: User = {firstname, lastname, email, password, picture} as User;

    // Send user to server
    this.userService.update(user)
      .pipe(first())
      .subscribe(
        data => {
          this.data = data;
          this.alertService.success("User's update successful.", true);
        },
        error => {
          this.alertService.error(error.message);
        }
      );
  }

  onImageChange($event) {
    console.debug("Inside \"onImageChange()\"!");

    if ( this.fileUploader.onImageChange($event) ) {  // If the file gets accepted.

      // Send photo to backend
      this.fileUploader.postFile(this.user.email);

      this.alertService.success("Image has changed! Reload the page to see it!", true); // It doesn't get shown.. why?

      // TODO - Find a way to automatically reload the page..
      // Tried the following ways but they don't work..

     /* if ( !this.router.navigateByUrl('/users/' + this.user.id + '/settings') )
        console.error("Navigation to \"\" has failed!");*/

      /*this.router.navigateByUrl('/users/' + this.user.id + '/home', {skipLocationChange: true}).then(()=>
        this.router.navigate(['/users/' + this.user.id + '/settings']));
       */

      //this.location.go('/users/' + this.user.id + '/settings')
      //this.location.replaceState('/users/' + this.user.id + '/home')
      //this.location.reload()  // It's not there now.. worked in previous versions of Angular.
    }
  }

  onImageReset() {
    console.debug("Going to reset the \"fileToUpload\".");
    this.fileUploader.onFileReset();
  }

}
