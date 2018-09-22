import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';

import { User, Experience, Education, CreationResponse } from '../../../_models/index';
import {
  UserService,
  ExperienceService,
  EducationService,
  AlertService,
  ConnectionConfigService
} from '../../../_services/index';
import { UserNavBarComponent } from './../user-nav-bar/user-nav-bar.component';
import { DateService } from "../../../_services/date.service";
import {DatePeriodValidatorDirective} from "../../../_directives/validators/date-period-validator.directive";


@Component({
  selector: 'app-home-user',
  templateUrl: './home-user.component.html',
  styleUrls: ['./home-user.component.scss']
})
export class HomeUserComponent implements OnInit {

  title = 'Home';
  currentUser: User;
  user: User;
  addExperienceForm: FormGroup;
  addEducationForm: FormGroup;
  submitted = false;
  public profilePhotosEndpoint: string;

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
      private connConfig: ConnectionConfigService
  ) {
    this.titleService.setTitle(this.title);
    this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
    this.profilePhotosEndpoint = this.connConfig.serverUrl + this.connConfig.userFilesEndpoint;

    // Occupy years array
    for (let i: number = 2018; i >= 1950; i--) {
      this.years.push({'id': i, 'name': i.toString()});
    }
  }

  ngOnInit() {
    this.getUserById(this.currentUser.id);

    // Initialise form contents
    this.addExperienceFormInit();
    this.addEducationFormInit();
  }

  private getUserById(id: number) {
    this.userService.getById(id).subscribe(user => { this.user = user; });
      // .subscribe((response: any) => {
      //     this.user = response.content;
      //   }, error => {
      //     this.alertService.error(error.error.message);
      //     console.log(error);
      //   }
      // );
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
      .pipe(
        first()
      )
      .subscribe((response: CreationResponse) => {
        // Alert the user
        this.alertService.success('Successfully added new experience.', false);
        this.submitted = false;

        // Add experience to the array
        if (response.object) {
          this.user.experience.push(response.object);
        }
      }, error => {
        this.alertService.error(error.error.message);
        console.log(error);
      }
    );
  }

  // Delete a specific experience
  deleteExperience(id: number) {
    this.experienceService.delete(id, this.user.id)
      .pipe(
        first()
      )
      .subscribe(response => {
        // Remove the experience from the array
        this.user.experience = this.user.experience.filter(item => item.id !== id);
      }, error => {
        this.alertService.error(error.error.message);
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
      .pipe(
        first()
      )
      .subscribe((response: CreationResponse) => {
          // Alert the user
          this.alertService.success('Successfully added new education.');
          this.submitted = false;

          // Add education to the array
          if (response.object) {
            this.user.education.push(response.object);
          }
        }, error => {
          this.alertService.error(error.error.message);
          console.log(error);
        }
    );
  }

  // Delete a specific education
  deleteEducation(id: number) {
    this.educationService.delete(id, this.user.id)
      .pipe(
        first()
      )
      .subscribe(response => {
        // Remove the education from the array
        this.user.education = this.user.education.filter(item => item.id !== id);
      }, error => {
        this.alertService.error(error.error.message);
      }
    );
  }

  clearAlert() {
    this.alertService.clear();
  }

}
