import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Title} from "@angular/platform-browser";
import {
  AlertService,
  ConnectionConfigService, DataService,
  EducationService,
  ExperienceService,
  UserService
} from "../../../_services";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {CreationResponse, Education, Experience, User} from "../../../_models";
import {DatePeriodValidatorDirective} from "../../../_directives/validators/date-period-validator.directive";
import {DateService} from "../../../_services/date.service";
import {first} from "rxjs/operators";

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss']
})
export class UserProfileComponent implements OnInit {

  title: string;
  userId: number;
  profilePhotosEndpoint: string;
  user: User;

  constructor(
    private titleService: Title,
    private route: ActivatedRoute,
    private userService: UserService,
    private experienceService: ExperienceService,
    private educationService: EducationService,
    private alertService: AlertService,
    private formBuilder: FormBuilder,
    private connConfig: ConnectionConfigService,
    private dataService: DataService
  ) {
    console.debug("Constructing \"UserProfileComponent\"!");
    this.route.params.subscribe( params => {
      this.userId = +params['id']; // (+) converts string 'id' to a number
       console.debug("UserID coming from url-parameters is:", this.userId);
    });
    console.debug("After the param..");
    this.getUserById(this.userId);
    console.debug("After the getByID..");

    // Occupy years array
    for (let i: number = 2018; i >= 1950; i--) {
      this.years.push({'id': i, 'name': i.toString()});
    }
  }

  ngOnInit() {
    console.debug("Initializing \"UserProfileComponent\"!");
  }

  private getUserById(id: number) {
    console.debug("Inside geById.");
    this.userService.getById(id).subscribe( user => {
      this.user = user;
      this.titleService.setTitle(this.user.firstname + ' ' + this.user.lastname);
      this.profilePhotosEndpoint = this.connConfig.usersEndpoint;
    },
error => {
        this.alertService.error(error.message);
        console.log(error);
      }
    );
  }

  currentUser: User;
  addExperienceForm: FormGroup;
  addEducationForm: FormGroup;
  submitted = false;
  message: string;

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
    let start = DateService.createDate(this.getAddExperienceForm.startYear.value, this.getAddExperienceForm.startMonth.value);
    let end = DateService.createDate(this.getAddExperienceForm.endYear.value, this.getAddExperienceForm.endMonth.value);

    // Create a new Experience object
    const newExperience: Experience = {
      title: this.getAddExperienceForm.title.value,
      company: this.getAddExperienceForm.company.value,
      startDate: start,
      endDate: end
    } as Experience;

    // Submit the experience to the server
    this.experienceService.create(newExperience, this.user.id)
      .pipe(first())
      .subscribe((response: CreationResponse) => {
          // Alert the user
          this.alertService.success('Successfully added new experience.', false);
          this.submitted = false;

          // Add experience to the array
          if (response.object) {
            this.user.experience.push(response.object);
          }
        }, error => {
          this.alertService.error(error.message);
          console.log(error);
        }
      );
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
    let start = DateService.createDate(this.getAddEducationForm.startYear.value, this.getAddEducationForm.startMonth.value);
    let end = DateService.createDate(this.getAddEducationForm.endYear.value, this.getAddEducationForm.endMonth.value);

    // Create a new Education object
    const newEducation: Education = {
      title: this.getAddEducationForm.title.value,
      school: this.getAddEducationForm.school.value,
      startDate: start,
      endDate: end
    } as Education;

    // Submit the education to the server
    this.educationService.create(newEducation, this.user.id)
      .pipe(first())
      .subscribe((response: CreationResponse) => {
          // Alert the user
          this.alertService.success('Successfully added new education.');
          this.submitted = false;

          // Add education to the array
          if (response.object) {
            this.user.education.push(response.object);
          }
        }, error => {
          this.alertService.error(error.message);
          console.log(error);
        }
      );
  }

  clearAlert() {
    this.alertService.clear();
  }


}
