import { Component, OnInit } from '@angular/core';
import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { AuthenticationService, DataService } from '../../../_services/index';

@Component({
  selector: 'app-user-nav-bar',
  templateUrl: './user-nav-bar.component.html',
  styleUrls: ['./user-nav-bar.component.scss']
})

@NgModule({
  schemas: [ CUSTOM_ELEMENTS_SCHEMA ]
})

export class UserNavBarComponent implements OnInit {

  message: string;

  constructor(
    private authenticationService: AuthenticationService,
    private dataService: DataService
  ) { }

  ngOnInit() {
    this.dataService.currentMessage.subscribe(message => this.message = message)
  }

  logout() {
    this.authenticationService.logout();
  }

}
