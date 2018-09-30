import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { Title } from "@angular/platform-browser";
import { ActivatedRoute } from "@angular/router";
import { first } from "rxjs/operators";
import {
  EducationService,
  ExperienceService,
  UserService,
  DateService,
  AlertService,
  ConnectionConfigService,
  FileUploaderService, AuthenticationService
} from "../../../_services";
import { DatePeriodValidatorDirective } from "../../../_directives/validators/date-period-validator.directive";
import { CreationResponse, Education, Experience, User } from "../../../_models";

@Component({
  selector: 'app-edit-profile',
  templateUrl: './user-info.component.html',
  styleUrls: ['./user-info.component.scss']
})
export class UserInfoComponent implements OnInit {

  title: string = 'Personal Information';
  signedInUser: User;
  public userInPath: User;
  addExperienceForm: FormGroup;
  addEducationForm: FormGroup;
  submitted = false;
  isAdmin: boolean;
  errorMessage = 'An error occurred!';

  public profilePhotosEndpoint: string;
  public userId: number;

  years: { id: number, name: string }[] = [];
  months = [
    {'id': 1, 'name': 'JAN'},
    {'id': 2, 'name': 'FEB'},
    {'id': 3, 'name': 'MAR'},
    {'id': 4, 'name': 'APR'},
    {'id': 5, 'name': 'MAY'},
    {'id': 6, 'name': 'JUN'},
    {'id': 7, 'name': 'JUL'},
    {'id': 8, 'name': 'AUG'},
    {'id': 9, 'name': 'SEP'},
    {'id': 10, 'name': 'OCT'},
    {'id': 11, 'name': 'NOV'},
    {'id': 12, 'name': 'DEC'}
  ];

  public constructor(
    private titleService: Title,
    private userService: UserService,
    private experienceService: ExperienceService,
    private educationService: EducationService,
    private alertService: AlertService,
    private formBuilder: FormBuilder,
    private connConfig: ConnectionConfigService,
    private route: ActivatedRoute,
    private fileUploader: FileUploaderService,
    private authenticationService: AuthenticationService
  ) {
    this.signedInUser = JSON.parse(localStorage.getItem('currentUser'));
    this.titleService.setTitle(this.title);

    this.route.params.subscribe(params => {
      this.userId = +params['id'];
      this.authenticationService.forbidUnauthorizedAccess(this.signedInUser, this.userId);
      this.getUserById(this.userId);
    });

    this.isAdmin = (this.signedInUser.email == 'admin@mail.com');

    // Occupy years array
    for (let i: number = 2018; i >= 1950; i--) {
      this.years.push({'id': i, 'name': i.toString()});
    }
  }

  ngOnInit() {
    // Initialise form contents
    this.addExperienceFormInit();
    this.addEducationFormInit();
  }

  private getUserById(id: number) {
    this.userService.getById(id).subscribe(
      user => {
        this.userInPath = user;
        this.profilePhotosEndpoint = this.connConfig.usersEndpoint + '/' + this.userInPath.id + '/photos';
      }, error => {
        this.alertService.error(error.error.errorMessage);
        console.log(error);
      }
    );
  }

  // Initiliases the form to add a new experience
  addExperienceFormInit() {
    this.addExperienceForm = this.formBuilder.group({
      title: ['', Validators.required],
      company: ['', Validators.required],
      startMonth: ['', Validators.required],
      startYear: ['', Validators.required],
      endMonth: ['', Validators.required],
      endYear: ['', [Validators.required, DatePeriodValidatorDirective.validateDatePeriod]]
    });
  }

  // Convenience getter for easy access to form fields
  get getAddExperienceForm() { return this.addExperienceForm.controls; }

  // Adds a new experience
  addExperience() {
    this.submitted = true;

    // If form is invalid stop here
    if (this.addExperienceForm.invalid) {
      return;
    }

    // Create the start and end dates
    let start = DateService.createDate(this.getAddExperienceForm.startYear.value,
        this.getAddExperienceForm.startMonth.value);
    let end = DateService.createDate(this.getAddExperienceForm.endYear.value,
        this.getAddExperienceForm.endMonth.value);

    // Create a new Experience object
    const newExperience: Experience = {
      title: this.getAddExperienceForm.title.value,
      company: this.getAddExperienceForm.company.value,
      startDate: start,
      endDate: end
    } as Experience;

    // Submit the experience to the server
    this.experienceService.create(newExperience, this.userInPath.id)
      .pipe(first())
      .subscribe((response: CreationResponse) => {
          // Alert the user
          this.alertService.success('Successfully added new experience.', false);
          this.submitted = false;

          // Add experience to the array
          if (response.object) {
            this.userInPath.experience.push(response.object);
          }
        }, error => {
          this.alertService.error(error.error.errorMessage);
          console.log(error);
        }
      );
  }

  // Delete a specific experience
  deleteExperience(id: number) {
    this.experienceService.delete(id, this.userInPath.id)
      .pipe(first())
      .subscribe(response => {
          // Remove the experience from the array
          this.userInPath.experience = this.userInPath.experience.filter(item => item.id !== id);
        }, error => {
          this.alertService.error(error.error.errorMessage);
        }
      );
  }

  // Initiliases the form to add a new education
  addEducationFormInit() {
    this.addEducationForm = this.formBuilder.group({
      title: ['', Validators.required],
      school: ['', Validators.required],
      startMonth: ['', Validators.required],
      startYear: ['', Validators.required],
      endMonth: ['', Validators.required],
      endYear: ['', [Validators.required, DatePeriodValidatorDirective.validateDatePeriod]]
    });
  }

  // Convenience getter for easy access to form fields
  get getAddEducationForm() { return this.addEducationForm.controls; }

  // Adds a new education
  addEducation() {
    this.submitted = true;

    // If form is invalid stop here
    if (this.addEducationForm.invalid) {
      return;
    }

    // Create the start and end dates
    let start = DateService.createDate(this.getAddEducationForm.startYear.value,
        this.getAddEducationForm.startMonth.value);
    let end = DateService.createDate(this.getAddEducationForm.endYear.value,
        this.getAddEducationForm.endMonth.value);

    // Create a new Education object
    const newEducation: Education = {
      title: this.getAddEducationForm.title.value,
      school: this.getAddEducationForm.school.value,
      startDate: start,
      endDate: end
    } as Education;

    // Submit the education to the server
    this.educationService.create(newEducation, this.userInPath.id)
      .pipe(first())
      .subscribe((response: CreationResponse) => {
          // Alert the user
          this.alertService.success('Successfully added new education.');
          this.submitted = false;

          // Add education to the array
          if (response.object) {
            this.userInPath.education.push(response.object);
          }
        }, error => {
          this.alertService.error(error.error.errorMessage);
          console.log(error);
        }
      );
  }

  // Delete a specific education
  deleteEducation(id: number) {
    this.educationService.delete(id, this.userInPath.id)
      .pipe(first())
      .subscribe(response => {
          // Remove the education from the array
          this.userInPath.education = this.userInPath.education.filter(item => item.id !== id);
        }, error => {
          this.alertService.error(error.error.errorMessage);
        }
      );
  }

  clearAlert() {
    this.alertService.clear();
  }


  onImageChange($event)
  {
    if ( this.fileUploader.onImageChange($event) )  // If the file gets accepted.
    {
      // Send photo to backend
      this.fileUploader.postFile(this.userInPath.email).subscribe(
        data => {
          this.alertService.success("Profile photo has changed!", true);
          this.reloadPage();
        },
        error => {
          this.alertService.error(error.error.errorMessage);
          console.log(error);
        }
      );
    }
  }

  onImageReset() {
    console.debug("Going to reset the \"fileToUpload\".");
    this.fileUploader.onFileReset();
  }

  reloadPage() {
    this.alertService.clear();
    window.history.go(0);  // 0 => go to the same page (current).
  }

}
