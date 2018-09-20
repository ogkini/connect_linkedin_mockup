import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';

import { User, Experience, CreationResponse } from '../../../_models/index';
import { UserService, ExperienceService, AlertService } from '../../../_services/index';
import { UserNavBarComponent } from './../user-nav-bar/user-nav-bar.component';

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
  submitted = false;

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
      private alertService: AlertService,
      private formBuilder: FormBuilder
  ) {
    this.titleService.setTitle(this.title);
    this.currentUser = JSON.parse(localStorage.getItem('currentUser'));

    // Occupy years array
    for (let i: number = 2018; i >= 1950; i--) {
      this.years.push({'id': i, 'name': i.toString()});
    }
  }

  ngOnInit() {
    this.getUserById(this.currentUser.id);

    // Initialise form contents
    this.addExperienceFormInit();
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
      endYear: ['', Validators.required]
    });
  }

  // Convenience getter for easy access to form fields
  get getAddExperienceForm() { return this.addExperienceForm.controls; }

  // Adds a new experience
  addExperience() {
    this.submitted = true;

    // Create the start and end dates
    let start = this.createDate(this.getAddExperienceForm.startYear.value, this.getAddExperienceForm.startMonth.value);
    let end = this.createDate(this.getAddExperienceForm.endYear.value, this.getAddExperienceForm.endMonth.value);

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
          this.alertService.success('Successfully added new experience.');

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

  // Creates and returns a valid date string
  private createDate(year: number, month: number) {
    if (month < 10)
      return `${year}-0${month}-01`;
    else
      return `${year}-${month}-01`;
  }

}
